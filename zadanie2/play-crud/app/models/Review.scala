package models

import play.api.libs.json.{Json, OFormat}

case class Review(id: Int, product: Long, customer: Long, content: String, score: Float)

object Review {
    implicit val reviewFormat: OFormat[Review] = Json.format[Review]
}
