package models

import play.api.libs.json.{Json}

case class Vehicle(wheels: Int ,
                   heavy: Boolean ,
                   name: String)

object Vehicle {
  implicit val formats = Json.format[Vehicle]
}