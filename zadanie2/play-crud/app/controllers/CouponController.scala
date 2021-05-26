package controllers

import models.Coupon
import models.repository.CouponRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats.{bigDecimalFormat, longFormat}
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext


@Singleton
class CouponController @Inject()(couponRepository: CouponRepository,
                                 controllerComponents: MessagesControllerComponents)
                                (implicit ec: ExecutionContext)
    extends MessagesAbstractController(controllerComponents) {

    val couponForm: Form[CreateCouponForm] = Form {
        mapping(
            "code" -> of[Long],
            "couponType" -> nonEmptyText,
            "discount" -> of[BigDecimal],
            "isActive" -> boolean,
            "createdAt" -> nonEmptyText,
            "usedAt" -> nonEmptyText
        )(CreateCouponForm.apply)(CreateCouponForm.unapply)
    }

    // VIEWS
    def getCouponsForm: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        val coupons = couponRepository.list()
        coupons.map( coupon => Ok(views.html.couponsList(coupon)))
    }

    // JSON METHODS
    def getCoupons: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        couponRepository.list().map { coupons =>
            Ok(Json.toJson(coupons)).as("application/json")
        }
    }

    def getCoupon(id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        couponRepository.getByIdOption(id: Int).map {
            case Some(coupons) => Ok(Json.toJson(coupons))
            case None => Redirect(routes.CouponController.getCoupons())
        }
    }

    def delete(id: Int): Action[AnyContent] = Action {
        couponRepository.delete(id)
        Redirect("/coupons")
    }

    def addCoupon(): Action[AnyContent] = Action { implicit request =>
        val coupon_json = request.body.asJson.get
        val coupon = coupon_json.as[Coupon]
        couponRepository.create(coupon.code, coupon.couponType, coupon.discount, coupon.isActive, coupon.createdAt, coupon.usedAt)
        Redirect("/coupons")
    }
}

case class CreateCouponForm(code: Long, couponType: String, discount: BigDecimal, isActive: Boolean, createdAt: String, usedAt: String)
