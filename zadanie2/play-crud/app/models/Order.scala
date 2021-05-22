package models

import play.api.libs.json.{Json, OFormat}

case class Order(id: Long, createdAt: String, customer: Long, isPaid: Boolean, paidAt: String, totalOrderValue: Int, couponCode: Long)

object Order {
    implicit val orderFormat: OFormat[Order] = Json.format[Order]
}
