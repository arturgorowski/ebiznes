package models

import play.api.libs.json.{Json, OFormat}

case class Order(id: Int,
                 customer: Int,
                 totalOrderValue: Float,
                 coupon: Int
                )

object Order {
    implicit val orderFormat: OFormat[Order] = Json.format[Order]
}
