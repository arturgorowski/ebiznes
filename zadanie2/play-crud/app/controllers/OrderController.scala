package controllers

import models.{Cart, Order}
import models.repository.OrderRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.api.data.format.Formats.floatFormat
import play.api.libs.json.Json

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class OrderController @Inject()(orderRepository: OrderRepository,
                                controllerComponents: MessagesControllerComponents)
                               (implicit ec: ExecutionContext)
    extends MessagesAbstractController(controllerComponents) {

    val cartForm: Form[CreateOrderForm] = Form {
        mapping(
            "createdAt" -> nonEmptyText,
            "customer" -> number,
            "isPaid" -> boolean,
            "paidAt" -> nonEmptyText,
            "totalOrderValue" -> of[Float],
            "coupon" -> number
        )(CreateOrderForm.apply)(CreateOrderForm.unapply)
    }

    // VIEWS
    def getOrdersForm: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        val orders = orderRepository.list()
        orders.map( order => Ok(views.html.ordersList(order)))
    }

    // JSON METHODS
    def getOrders: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        orderRepository.list().map { carts =>
            Ok(Json.toJson(carts)).as("application/json")
        }
    }

    def getCustomerOrder(customer_id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        orderRepository.getByCustomer(customer_id: Int).map {
            case Some(orders) => Ok(Json.toJson(orders)).as("application/json")
            case None => Ok(Json.toJson(AnyContentAsEmpty.asJson))
        }
    }

    def getOrder(id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        orderRepository.getByIdOption(id: Int).map {
            case Some(orders) => Ok(Json.toJson(orders))
            case None => Ok(Json.toJson(AnyContentAsEmpty.asJson))
        }
    }

    def delete(id: Int): Action[AnyContent] = Action {
        orderRepository.delete(id)
        Redirect("/carts")
    }

    def addOrder(): Action[AnyContent] = Action { implicit request =>
        val order_json = request.body.asJson.get
        val order = order_json.as[Order]
        orderRepository.create(order.createdAt, order.customer, order.isPaid, order.paidAt, order.totalOrderValue, order.coupon)
        Redirect("/carts" + order.customer)
    }
}

case class CreateOrderForm(createdAt: String, customer: Int, isPaid: Boolean, paidAt: String, totalOrderValue: Float, coupon: Int)
