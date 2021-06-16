package controllers

import models.Address
import models.repository.AddressRepository

import javax.inject._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class AddressController @Inject()(addressRepository: AddressRepository,
                                  controllerComponents: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext)
    extends MessagesAbstractController(controllerComponents) {

    val addressForm: Form[CreateAddressForm] = Form {
        mapping(
            "street" -> nonEmptyText,
            "number" -> number,
            "city" -> nonEmptyText,
            "postalCode" -> nonEmptyText,
            "voivodeship" -> nonEmptyText
        )(CreateAddressForm.apply)(CreateAddressForm.unapply)
    }

    // VIEWS
    def getAddressesForm: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        val addresses = addressRepository.list()
        addresses.map( address => Ok(views.html.addressList(address)))
    }

    // JSON METHODS
    def getAddresses: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        addressRepository.list().map { address =>
            Ok(Json.toJson(address))
        }
    }

    def getAddress(id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        addressRepository.getByOptionId(id: Int).map {
            case Some(address) => Ok(Json.toJson(address))
            case None => Redirect(routes.AddressController.getAddresses())
        }
    }

    def delete(id: Int): Action[AnyContent] = Action {
        addressRepository.delete(id)
        Redirect("/addresses")
    }

    def addProduct(): Action[AnyContent] = Action { implicit request =>
        val addressJson = request.body.asJson.get
        val address = addressJson.as[Address]
        addressRepository.create(address.street, address.number, address.city, address.postalCode, address.voivodeship)
        Redirect("/addresses")
    }

}

case class CreateAddressForm(street: String, number: Int, city: String, postalCode: String, voivodeship: String)
