package controllers

import javax.inject._
import models.Product
import models.repository.ProductRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats.floatFormat
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.ExecutionContext

@Singleton
class ProductController @Inject()(productRepository: ProductRepository,
                                  controllerComponents: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext)
    extends MessagesAbstractController(controllerComponents) {

    val appJson = "application/json";

    val productForm: Form[CreateProductForm] = Form {
        mapping(
            "name" -> nonEmptyText,
            "description" -> nonEmptyText,
            "price" -> of[Float],
            "category" -> number
        )(CreateProductForm.apply)(CreateProductForm.unapply)
    }

    // VIEWS
    def getProductsForm: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        val products = productRepository.list()
        products.map( product => Ok(views.html.productsList(product)))
    }

    // JSON METHODS
    def getProducts: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        productRepository.list().map { products =>
            Ok(Json.toJson(products)).as(appJson)
        }
    }

//    def getProductByCategory(category_id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
//        productRepository.getByCategoryOption(category_id: Int).map {
//            case Some(reviews) => Ok(Json.toJson(reviews)).as("application/json")
//            case None => Ok(Json.toJson(AnyContentAsEmpty.asJson))
//        }
//    }

    def getProductByCategory(categoryId: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        productRepository.getByCategory(categoryId: Int).map {
            reviews => Ok(Json.toJson(reviews)).as(appJson)
        }
    }

    def getProduct(id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        productRepository.getByIdOption(id: Int).map {
            case Some(products) => Ok(Json.toJson(products))
            case None => Redirect(routes.ProductController.getProducts())
        }
    }

    def delete(id: Int): Action[AnyContent] = Action {
        productRepository.delete(id)
        Redirect("/products")
    }

    def addProduct(): Action[AnyContent] = Action { implicit request =>
        val productJson = request.body.asJson.get
        val product = productJson.as[Product]
        productRepository.create(product.name, product.description, product.price, product.category)
        Redirect("/products")
    }
}

case class CreateProductForm(name: String, description: String, price: Float, category: Int)
