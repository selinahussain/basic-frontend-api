package controllers

import javax.inject._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def string( element: String) = Action { implicit request: Request[AnyContent] =>
    println("Here" + element)
    Ok(views.html.string(element))
  }

  def checkVehicle( vehicle: String) = Action { implicit request: Request[AnyContent] =>
    val carObj = Json.obj(
      "wheels" -> "4",
      "heavy"  -> true,
      "name" -> "Bmw"
    )

    val bikeObj = Json.obj(
      "wheels" -> "2",
      "heavy"-> false,
      "name" -> "Chopper"
    )

    val car = new Vehicle(wheels = 4, heavy= true, name= "BMW")


    vehicle match{
      case "car" =>  Ok(views.html.vehicles(car))
      case "bike" =>  Ok(Json.prettyPrint(bikeObj))
      case _ => Ok(views.html.string("Please specify a vehicle"))
    }
  }
}

class Vehicle(val wheels: Int, val heavy : Boolean, val name: String){


}
