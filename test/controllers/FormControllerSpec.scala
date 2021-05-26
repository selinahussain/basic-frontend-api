package controllers

import org.mockito.ArgumentMatchers
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.ws._
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito.when
import play.api.libs.json.{JsObject, Json}

import scala.concurrent.{ExecutionContext, Future}

class FormControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit lazy val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]

  lazy val ws: WSClient = app.injector.instanceOf[WSClient]

  lazy val wsMock: WSClient = mock[WSClient]
  lazy val wsRequest: WSRequest = mock[WSRequest]
  lazy val wsResponse = mock[WSResponse]





  "FormController simpleForm " should {

    "render the simpleForm page from a new instance of controller" in {
      val controller = new FormController(ws, stubControllerComponents(), executionContext)
      val simpleForm = controller.simpleForm().apply(FakeRequest(GET, "/simpleForm"))

      status(simpleForm) mustBe OK
      contentType(simpleForm) mustBe Some("text/html")
      contentAsString(simpleForm) must include ("Vehicle Search")
    }

    "render the simpleForm page from the application" in {
      val controller = inject[FormController]
      val home = controller.simpleForm().apply(FakeRequest(GET, "/simpleForm"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Vehicle Search")
    }

  }

  "FormController simpleFormPost() " should {

    "render the simpleFormPost page from a new instance of controller " in {
      when(wsMock.url(ArgumentMatchers.any())) thenReturn wsRequest
      when(wsResponse.status) thenReturn 200
      when(wsResponse.json) thenReturn Json.parse(
        """{
          | "wheels": 4,
          | "heavy": true,
          | "name": "BMW"
          |}""".stripMargin)
      when(wsRequest.post(any[JsObject]())(any[BodyWritable[JsObject]]())) thenReturn Future.successful(wsResponse)

      lazy val controller = new FormController(wsMock, stubControllerComponents(), executionContext)
      lazy val result = controller.simpleFormPost().apply(FakeRequest(POST, "/simpleForm"))


      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include ("Vehicle")
    }

    "fail to render the SimplePost  from the application" in {
      when(wsMock.url(ArgumentMatchers.any())) thenReturn wsRequest
      when(wsResponse.status) thenReturn 404
      when(wsResponse.json) thenReturn Json.parse(
        """{

          }""")

      when(wsRequest.post(any[JsObject]())(any[BodyWritable[JsObject]]())) thenReturn Future.failed(new Exception)

      lazy val controller = new FormController(wsMock, stubControllerComponents(), executionContext)
      lazy val result = controller.simpleFormPost().apply(FakeRequest(POST, "/simpleForm"))


      status(result) mustBe NOT_FOUND
    }

  }

}
