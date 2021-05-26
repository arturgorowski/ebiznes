package models

import play.api.libs.json.{Json, OFormat}

case class Product(id: Int,
                   name: String,
                   description: String,
                   price: Float,
                   category: Int
                  )

object Product {
    implicit val productFormat: OFormat[Product] = Json.format[Product]

}