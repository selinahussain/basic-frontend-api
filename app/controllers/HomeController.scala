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

//  def string( element: String) = Action { implicit request: Request[AnyContent] =>
//    println("Here" + element)
//    Ok(views.html.text(element))
//  }

  def checkVehicle( vehicle: String) = Action { implicit request: Request[AnyContent] =>


    val car = new Vehicle(4, true,"BMW")
    val bike = new Vehicle(2, false, "Chopper")
    val empty = Empty


    vehicle match{
      case "car" =>  Ok(views.html.text(car, ""))
      case "bike" =>  Ok(views.html.text(bike, ""))
      case _ => Ok(views.html.text(empty,"Not a vehicle"))
    }
  }
}

case class Vehicle( wheels: Int ,  heavy: Boolean ,  name: String ) {
}

object Empty extends Vehicle(null.asInstanceOf[Int], null.asInstanceOf[Boolean], null.asInstanceOf[String]){
}