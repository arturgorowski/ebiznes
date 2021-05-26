package models

import play.api.libs.json.{Json, OFormat}

case class Cart(id: Int,
                customer: Int,
                productsQuantity: Int,
                totalProductsPrice: BigDecimal,
                coupon: Int,
                createdAt: String
               )

object Cart {
    implicit val cartFormat: OFormat[Cart] = Json.format[Cart]
}
