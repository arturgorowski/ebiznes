package models

import play.api.libs.json.{Json, OFormat}

case class Address(id: Int,
                   street: String,
                   number: Int,
                   city: String,
                   postalCode: String,
                   voivodeship: String
                  )

object Address {
    implicit val addressFormat: OFormat[Address] = Json.format[Address]
}
