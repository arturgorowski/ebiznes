package controllers

import javax.inject._
import models.{CartItem, Product}
import models.repository.{CartItemRepository, ProductRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.{Json, OFormat}
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

@Singleton
class CartItemController @Inject()(cartItemRepository: CartItemRepository,
                                   productRepository: ProductRepository,
                                  controllerComponents: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext)
    extends MessagesAbstractController(controllerComponents) {

    val appJson = "application/json";

    val cartItemFormForm: Form[CreateCartItemForm] = Form {
        mapping(
            "cart" -> number,
            "product" -> number,
            "productQuantity" -> number
        )(CreateCartItemForm.apply)(CreateCartItemForm.unapply)
    }

    // VIEWS
    def getCartItemsForm: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        val cartItems = cartItemRepository.list()
        cartItems.map( cartItem => Ok(views.html.cartItemsList(cartItem)))
    }

    // JSON METHODS
//    def getCartItems: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
//        cartItemRepository.list().map { cartItems =>
//            Ok(Json.toJson(cartItems)).as("application/json")
//        }
//    }

    def getCartItems(cartId: Int): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        val cartItems = Await.ready(cartItemRepository.getByCart(cartId: Int), Duration.Inf).value.get.get

        implicit var cartItemModel: List[CartItemProductModel] = List[CartItemProductModel]()
        cartItems.foreach{ cartItem =>
            val product = Await.ready(productRepository.getById(cartItem.product), Duration.Inf).value.get.get
            val newCartItem = CartItemProductModel(cartItem.id, product, cartItem.productQuantity)
            cartItemModel = newCartItem :: cartItemModel
        }
        Ok(Json.toJson(cartItemModel))
    }

    def getCartItem(id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        cartItemRepository.getById(id: Int).map { cartItem =>
            Ok(Json.toJson(cartItem)).as(appJson)
        }
    }

    def delete(id: Int, cartId: Int): Action[AnyContent] = Action {
        cartItemRepository.delete(id)
        Redirect("/cartitems/" + cartId)
    }

    def addCartItem(): Action[AnyContent] = Action { implicit request =>
        val cartItemsJson = request.body.asJson.get
        val cartItem = cartItemsJson.as[CartItem]
        cartItemRepository.create(cartItem.cart, cartItem.product, cartItem.productQuantity)
        Ok(Json.toJson("Produkt dodano do koszyka")).as(appJson)
    }
}

case class CreateCartItemForm(cart: Int, product: Int, productQuantity: Int)


case class CartItemProductModel(id: Int,
                                product: Product,
                                productQuantity: Int)

object CartItemProductModel{
    implicit val cartItemFormat: OFormat[CartItemProductModel] = Json.format[CartItemProductModel]
}
