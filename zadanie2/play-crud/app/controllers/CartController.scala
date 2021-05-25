package controllers

import play.api.mvc._

import javax.inject._


@Singleton
class CartController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

    def index() = Action { implicit request: Request[AnyContent] =>
        Ok(views.html.index())
    }
}
