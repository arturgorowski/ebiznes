package models

import play.api.libs.json.{Json, OFormat}

case class OrderItem(id: Long, order: Long, product: Long)

object OrderItem {
    implicit val orderItemFormat: OFormat[OrderItem] = Json.format[OrderItem]
}
