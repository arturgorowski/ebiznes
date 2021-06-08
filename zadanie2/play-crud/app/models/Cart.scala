package models

import play.api.libs.json.{Json, OFormat}

case class Cart(id: Int,
                customer: Int,
                totalProductsPrice: Float,
                coupon: Int
               )

object Cart {
    implicit val cartFormat: OFormat[Cart] = Json.format[Cart]
}
