package controllers

import javax.inject._
import play.api._
import play.api.libs.json.{JsObject, JsValue, Json}
import models.Vehicle

import play.api.mvc._
import play.api.libs.ws._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(ws: WSClient, val controllerComponents: ControllerComponents, implicit val ec: ExecutionContext) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def index(vehicleName: String) = Action.async { implicit request: Request[AnyContent] =>

    val futureResult = ws.url(s"http://localhost:9001/checkVehicle/${vehicleName}").get()

    futureResult.map { response =>
      val veh = Json.fromJson[Vehicle](response.json).get
      Ok(views.html.vehicle(veh))

    }

  }


}

