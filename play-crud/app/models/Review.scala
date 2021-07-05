package models

import play.api.libs.json.{Json, OFormat}

case class Review(id: Int,
                  product: Int,
                  customer: Int,
                  content: String,
                  score: Float
                 )

object Review {
    implicit val customerFormat: OFormat[Customer] = Json.format[Customer]
    implicit val reviewFormat: OFormat[Review] = Json.format[Review]
}
