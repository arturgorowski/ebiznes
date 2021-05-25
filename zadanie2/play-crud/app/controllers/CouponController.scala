package controllers

import play.api.mvc._

import javax.inject._


@Singleton
class CouponController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

    def index() = Action { implicit request: Request[AnyContent] =>
        Ok(views.html.index())
    }
}
