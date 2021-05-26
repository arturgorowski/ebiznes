package models

import play.api.libs.json.{Json, OFormat}

case class Order(id: Int,
                 createdAt: String,
                 customer: Int,
                 isPaid: Boolean,
                 paidAt: String,
                 totalOrderValue: Int,
                 coupon: Int
                )

object Order {
    implicit val orderFormat: OFormat[Order] = Json.format[Order]
}
