package controllers

import models.{CartItem, Order, Product}
import models.repository.{CartItemRepository, CartRepository, OrderItemRepository, OrderRepository, ProductRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.api.data.format.Formats.floatFormat
import play.api.libs.json.{Json, OFormat}

import javax.inject._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

@Singleton
class OrderController @Inject()(orderRepository: OrderRepository,
                                orderItemRepository: OrderItemRepository,
                                cartRepository: CartRepository,
                                cartItemRepository: CartItemRepository,
                                productRepository: ProductRepository,
                                controllerComponents: MessagesControllerComponents)
                               (implicit ec: ExecutionContext)
    extends MessagesAbstractController(controllerComponents) {

    val appJson = "application/json";

    val cartForm: Form[CreateOrderForm] = Form {
        mapping(
            "customer" -> number,
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
            Ok(Json.toJson(carts)).as(appJson)
        }
    }

    def getCustomerOrder(customerId: Int): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        val orders = Await.ready(orderRepository.getByCustomer(customerId), Duration.Inf).value.get.get

        implicit var ordersModel: List[OrderModelForFront] = List[OrderModelForFront]()
        orders.foreach{ order =>
            val orderItems = Await.ready(orderItemRepository.getByOrder(order.id), Duration.Inf).value.get.get

            implicit var orderedProducts: List[Product] = List[Product]()
            orderItems.foreach{ orderItem =>
                val product = Await.ready(productRepository.getById(orderItem.product), Duration.Inf).value.get.get
                orderedProducts = product :: orderedProducts
            }
            ordersModel = OrderModelForFront(order.id, order.customer, order.totalOrderValue, order.coupon, orderedProducts) :: ordersModel
        }
        Ok(Json.toJson(ordersModel))
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
        val orderJson = request.body.asJson.get
        val order = orderJson.as[OrderModel]
        val newOrder: Order = Await.ready(orderRepository.create(order.customer, order.totalOrderValue, order.coupon), Duration.Inf).value.get.get

        order.products.foreach{ product =>
            Await.ready(orderItemRepository.create(newOrder.id, product.id), Duration.Inf).value.get.get
        }

        val cartItems = Await.ready(cartItemRepository.getByCart(order.cartId), Duration.Inf).value.get.get
        cartItems.foreach{ cartItem =>
            Await.ready(cartItemRepository.delete(cartItem.id), Duration.Inf).value.get.get
        }
        Await.ready(cartRepository.delete(order.cartId), Duration.Inf).value.get.get

        Ok(Json.toJson("Pomy??lnie z??o??ono zam??wienie")).as(appJson)
    }
}

case class CreateOrderForm(customer: Int, totalOrderValue: Float, coupon: Int)

object OrderModel {
    implicit val orderFormat: OFormat[OrderModel] = Json.format[OrderModel]
}
case class OrderModel(id: Int,
                      customer: Int,
                      totalOrderValue: Float,
                      coupon: Int,
                      cartId: Int,
                      products: List[Product])

object OrderModelForFront {
    implicit val orderFormat: OFormat[OrderModelForFront] = Json.format[OrderModelForFront]
}
case class OrderModelForFront(id: Int,
                      customer: Int,
                      totalOrderValue: Float,
                      coupon: Int,
                      products: List[Product])


