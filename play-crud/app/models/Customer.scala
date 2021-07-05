package models

import play.api.libs.json.{Json, OFormat}

case class Customer(id: Int,
                    username: String,
                    firstName: String,
                    lastName: String,
                    userId: Int,
                    address: Int
                   )

object Customer {
    implicit val customerFormat: OFormat[Customer] = Json.format[Customer]
}
