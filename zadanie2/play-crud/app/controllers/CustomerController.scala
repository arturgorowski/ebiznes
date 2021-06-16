package controllers

import models.Customer
import models.repository.CustomerRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext


@Singleton
class CustomerController @Inject()(customerRepository: CustomerRepository,
                                   controllerComponents: MessagesControllerComponents)
                                  (implicit ec: ExecutionContext)
    extends MessagesAbstractController(controllerComponents) {

    val productForm: Form[CreateCustomerForm] = Form {
        mapping(
            "username" -> nonEmptyText,
            "firstName" -> nonEmptyText,
            "lastName" -> nonEmptyText,
            "password" -> nonEmptyText,
            "createdAt" -> nonEmptyText,
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
            Ok(Json.toJson(products)).as("application/json")
        }
    }

    def getCustomer(id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        customerRepository.getByIdOption(id: Int).map {
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
        customerRepository.create(customer.username, customer.firstName, customer.lastName, customer.password, customer.createdAt, customer.address)
        Redirect("/customers")
    }
}

case class CreateCustomerForm(username: String, firstName: String, lastName: String, password: String, createdAt: String, address: Int)
