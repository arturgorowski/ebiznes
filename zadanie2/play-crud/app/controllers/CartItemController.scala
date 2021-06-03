package controllers

import javax.inject._
import models.CartItem
import models.repository.CartItemRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.ExecutionContext

@Singleton
class CartItemController @Inject()(cartItemRepository: CartItemRepository,
                                  controllerComponents: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext)
    extends MessagesAbstractController(controllerComponents) {

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

    def getCartItems(cart_id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        cartItemRepository.getByCart(cart_id: Int).map { cartItems =>
            Ok(Json.toJson(cartItems)).as("application/json")
        }
    }

    def getCartItem(id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        cartItemRepository.getById(id: Int).map { cartItem =>
            Ok(Json.toJson(cartItem)).as("application/json")
        }
    }

    def delete(id: Int, cart_id: Int): Action[AnyContent] = Action {
        cartItemRepository.delete(id)
        Redirect("/cartItems/" + cart_id)
    }

    def addCartItem(): Action[AnyContent] = Action { implicit request =>
        val cartItems_json = request.body.asJson.get
        val cartItem = cartItems_json.as[CartItem]
        cartItemRepository.create(cartItem.cart, cartItem.product, cartItem.productQuantity)
        Redirect("/cartitems/" + cartItem.cart)
    }
}

case class CreateCartItemForm(cart: Int, product: Int, productQuantity: Int)
