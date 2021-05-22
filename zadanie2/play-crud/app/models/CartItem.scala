package models

import play.api.libs.json.{Json, OFormat}

case class CartItem(id: Long, cart: Long, product: Long, productQuantity: Int)

object CartItem {
    implicit val cartItemFormat: OFormat[CartItem] = Json.format[CartItem]
}
