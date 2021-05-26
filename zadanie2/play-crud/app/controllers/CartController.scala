package controllers

import javax.inject._
import models.Cart
import models.repository.CartRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats.bigDecimalFormat
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class CartController @Inject()(cartRepository: CartRepository,
                               controllerComponents: MessagesControllerComponents)
                              (implicit ec: ExecutionContext)
    extends MessagesAbstractController(controllerComponents) {

    val cartForm: Form[CreateCartForm] = Form {
        mapping(
            "customer" -> number,
            "productsQuantity" -> number,
            "totalProductsPrice" -> of[BigDecimal],
            "coupon" -> number,
            "createdAt" -> nonEmptyText
        )(CreateCartForm.apply)(CreateCartForm.unapply)
    }

    // VIEWS
    def getCartsForm: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        val carts = cartRepository.list()
        carts.map( cart => Ok(views.html.cartsList(cart)))
    }

    // JSON METHODS
    def getCarts: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        cartRepository.list().map { carts =>
            Ok(Json.toJson(carts)).as("application/json")
        }
    }

    def getCart(id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        cartRepository.getByIdOption(id: Int).map {
            case Some(carts) => Ok(Json.toJson(carts))
            case None => Redirect(routes.CartController.getCarts())
        }
    }

    def delete(id: Int): Action[AnyContent] = Action {
        cartRepository.delete(id)
        Redirect("/carts")
    }

    def addCart(): Action[AnyContent] = Action { implicit request =>
        val cart_json = request.body.asJson.get
        val cart = cart_json.as[Cart]
        cartRepository.create(cart.customer, cart.productsQuantity, cart.totalProductsPrice, cart.coupon, cart.createdAt)
        Redirect("/carts")
    }
}

case class CreateCartForm(customer: Int, productsQuantity: Int, totalProductsPrice: BigDecimal, coupon: Int, createdAt: String)