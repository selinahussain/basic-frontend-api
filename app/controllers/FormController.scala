package controllers
import models.Vehicle
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.libs.ws._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class FormController @Inject()(ws: WSClient, cc: ControllerComponents, implicit val ec: ExecutionContext) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def simpleForm() = Action {  implicit request: Request[AnyContent] =>
    Ok(views.html.form(BasicForm.form))
  }

  def simpleFormPost(): Action[AnyContent] = Action.async { implicit request =>
    val postData = request.body.asFormUrlEncoded

    val vehicleName = postData.map{args =>
        args("Vehicle Name").head
    }.getOrElse(Ok("Error"))

    val dataToBeSend = Json.obj(
      "Vehicle Name" -> s"${vehicleName}"
    )

    val futureResponse: Future[WSResponse] = ws.url("http://localhost:9001/form").post(dataToBeSend)

    futureResponse.map {
      response =>
        val js = Json.fromJson[Vehicle](response.json)
        val veh = js.get
        Ok(views.html.vehicle(veh))
    } recover {
      case _ => NotFound
    }
  }
}

case class BasicForm(
                     name: String)

object BasicForm {
  val form: Form[BasicForm] = Form(
    mapping(
      "name" -> text
    )(BasicForm.apply)(BasicForm.unapply)
  )
}