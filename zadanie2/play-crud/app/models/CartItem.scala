package models

import play.api.libs.json.{Json, OFormat}

case class CartItem(id: Int,
                    cart: Int,
                    product: Int,
                    productQuantity: Int
                   )

object CartItem {
    implicit val cartItemFormat: OFormat[CartItem] = Json.format[CartItem]
}
