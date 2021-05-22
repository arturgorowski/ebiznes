package models

import play.api.libs.json.{Json, OFormat}

case class Cart(id: Long, customer: Long, productsQuantity: Int, totalProductsPrice: BigDecimal, coupon: Long, createdAt: String)

object Cart {
    implicit val cartFormat: OFormat[Cart] = Json.format[Cart]
}
