package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.ws._

import scala.concurrent.ExecutionContext



/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HomeControllerSpec() extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit lazy val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  lazy val ws: WSClient = app.injector.instanceOf[WSClient]

  "HomeController GET" should {

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
      val controller = inject[HomeController]
      val vehicle = controller.vehicle("BMW").apply(FakeRequest(GET, "/vehicle/BMW"))

      status(vehicle) mustBe OK
      contentType(vehicle) mustBe Some("text/html")
      contentAsString(vehicle) must include ("Welcome to Vehicle")
    }

    "fail to render the vehicle page from the application" in {
      val controller = inject[HomeController]
      val vehicle = controller.vehicle("test").apply(FakeRequest(GET, "/vehicle/test"))

      status(vehicle) mustBe NOT_FOUND
    }

    "render the vehicle page from the router" in {

      val request = FakeRequest(GET, "/vehicle/BMW")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Vehicle")
    }


  }

}
