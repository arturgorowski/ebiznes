package models

import play.api.libs.json.{Json, OFormat}

case class OrderItem(id: Int,
                     order: Int,
                     product: Int
                    )

object OrderItem {
    implicit val orderItemFormat: OFormat[OrderItem] = Json.format[OrderItem]
}
