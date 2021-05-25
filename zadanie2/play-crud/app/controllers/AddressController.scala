package controllers

import models.repository.AddressRepository

import javax.inject._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global


@Singleton
class AddressController @Inject()(val controllerComponents: ControllerComponents,
                                  addressRepository: AddressRepository) extends BaseController {

    def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        Ok(views.html.index())
    }

    def getAddresses: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        addressRepository.list().map { products =>
            Ok(Json.toJson(products))
        }
    }

//    def getAddress(id: Long): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
//        addressRepository.getById(id).map { products =>
//            Ok(Json.toJson(products))
//        }
//    }

}
