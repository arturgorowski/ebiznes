package models

import play.api.libs.json.{Json, OFormat}

case class Coupon(id: Int,
                  code: Long,
                  couponType: String,
                  discount: Float,
                  isActive: Boolean,
                  createdAt: String,
                  usedAt: String
                 )

object Coupon {
    implicit val couponFormat: OFormat[Coupon] = Json.format[Coupon]
}
