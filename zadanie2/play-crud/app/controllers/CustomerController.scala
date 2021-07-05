package controllers

import models.Customer
import models.repository.CustomerRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}


@Singleton
class CustomerController @Inject()(customerRepository: CustomerRepository,
                                   controllerComponents: MessagesControllerComponents)
                                  (implicit ec: ExecutionContext)
    extends MessagesAbstractController(controllerComponents) {

    val appJson = "application/json";


    val productForm: Form[CreateCustomerForm] = Form {
        mapping(
            "username" -> nonEmptyText,
            "firstName" -> nonEmptyText,
            "lastName" -> nonEmptyText,
            "userId" -> number,
            "address" -> number,
        )(CreateCustomerForm.apply)(CreateCustomerForm.unapply)
    }

    // VIEWS
    def getCustomersForm: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        val customers = customerRepository.list()
        customers.map( customer => Ok(views.html.customersList(customer)))
    }

    // JSON METHODS
    def getCustomers: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        customerRepository.list().map { products =>
            Ok(Json.toJson(products)).as(appJson)
        }
    }

    def getCustomer(id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        customerRepository.getByIdOption(id: Int).map {
            case Some(customers) => Ok(Json.toJson(customers))
            case None => Redirect(routes.CustomerController.getCustomers())
        }
    }

    def getCustomerByUserId(userId: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        customerRepository.getByUserId(userId: Int).map {
            case Some(customers) => Ok(Json.toJson(customers))
            case None => Redirect(routes.CustomerController.getCustomers())
        }
    }

    def delete(id: Int): Action[AnyContent] = Action {
        customerRepository.delete(id)
        Redirect("/customers")
    }

    def addCustomer(): Action[AnyContent] = Action { implicit request =>
        val customerJson = request.body.asJson.get
        val customer = customerJson.as[Customer]

        Await.ready(customerRepository.create(customer.username, customer.firstName, customer.lastName, customer.userId, customer.address), Duration.Inf).value.get.get

        val customerObj = Await.ready(customerRepository.getByUserId(customer.userId: Int), Duration.Inf).value.get.get
        Ok(Json.toJson(customerObj)).as(appJson)
    }
}

case class CreateCustomerForm(username: String, firstName: String, lastName: String, userId: Int, address: Int)
