package controllers


import org.mockito.ArgumentMatchers
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.{ExecutionContext, Future}



/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HomeControllerSpec() extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit lazy val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  lazy val ws: WSClient = app.injector.instanceOf[WSClient]

  lazy val wsMock: WSClient = mock[WSClient]
  lazy val wsRequest: WSRequest = mock[WSRequest]
  lazy val wsResponse: WSResponse = mock[WSResponse]

  "HomeController get index " should {

    "render the index page from a new instance of controller" in {
      val controller = new HomeController(ws, stubControllerComponents(), executionContext)
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }

    "render the index page from the application" in {
      val controller = inject[HomeController]
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }
  }

  "HomeController GET/vehicle/vehiclename" should {

    "render the vehicle page from the application" in {

//      val controller = inject[HomeController]
//      val vehicle = controller.vehicle("BMW").apply(FakeRequest(GET, "/vehicle/BMW"))
//
//      when(wsRequest.get())
//        .thenReturn(Future.successful(wsResponse))

      when(wsMock.url(ArgumentMatchers.any())) thenReturn wsRequest
      when(wsResponse.status) thenReturn 200
      when(wsResponse.json) thenReturn Json.parse(
        """{
          | "wheels": 4,
          | "heavy": true,
          | "name": "BMW"
          |}""".stripMargin)
      when(wsRequest.get()) thenReturn Future(wsResponse)

      lazy val controller = new HomeController(wsMock, stubControllerComponents(), executionContext)
      lazy val result = controller.vehicle("BMW").apply(FakeRequest(GET, "/vehicle/BMW"))

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include ("Vehicle")

    }

    "fail to render the vehicle page from the application" in {
      val controller = inject[HomeController]
      val vehicle = controller.vehicle("test").apply(FakeRequest(GET, "/vehicle/test"))

      when(wsRequest.get())
        .thenReturn(Future.failed(new Exception))

      status(vehicle) mustBe NOT_FOUND
    }
//
//    "render the vehicle page from the router" in {
//
//      val request = FakeRequest(GET, "/vehicle/BMW")
//      val home = route(app, request).get
//
//      when(wsRequest.get())
//        .thenReturn(Future.successful(wsResponse))
//
//      status(home) mustBe OK
//      contentType(home) mustBe Some("text/html")
//      contentAsString(home) must include ("Welcome to Vehicle")
//    }


  }

}

