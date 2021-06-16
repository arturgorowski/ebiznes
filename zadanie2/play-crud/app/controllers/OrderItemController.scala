package controllers

import javax.inject._
import models.OrderItem
import models.repository.OrderItemRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.ExecutionContext

@Singleton
class OrderItemController @Inject()(orderItemRepository: OrderItemRepository,
                                  controllerComponents: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext)
    extends MessagesAbstractController(controllerComponents) {

    val appJson = "application/json";

    val cartItemFormForm: Form[CreateOrderItemForm] = Form {
        mapping(
            "order" -> number,
            "product" -> number
        )(CreateOrderItemForm.apply)(CreateOrderItemForm.unapply)
    }

    // VIEWS
    def getOrderItemsForm: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        val orderItems = orderItemRepository.list()
        orderItems.map( orderItem => Ok(views.html.orderItemsList(orderItem)))
    }

    // JSON METHODS
    def getOrderItems(orderId: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        orderItemRepository.getByOrder(orderId: Int).map { orderItems =>
            Ok(Json.toJson(orderItems)).as(appJson)
        }
    }

//    def getOrderItem(id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
//        orderItemRepository.getByIdOption(id: Int).map {
//            case Some(cartItems) => Ok(Json.toJson(cartItems)).as("application/json")
//            case None => Redirect(routes.OrderItemController.getOrderItems(id: Int))
//        }
//    }

    def getOrderItem(id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        orderItemRepository.getById(id: Int).map { orderItem =>
            Ok(Json.toJson(orderItem)).as(appJson)
        }
    }

    def delete(id: Int, orderId: Int): Action[AnyContent] = Action {
        orderItemRepository.delete(id)
        Redirect("/orderItems/" + orderId)
    }

    def addOrderItem(): Action[AnyContent] = Action { implicit request =>
        val orderItemsJson = request.body.asJson.get
        val orderItem = orderItemsJson.as[OrderItem]
        orderItemRepository.create(orderItem.order, orderItem.product)
        Redirect("/orderItems")
    }
}

case class CreateOrderItemForm(order: Int, product: Int)
