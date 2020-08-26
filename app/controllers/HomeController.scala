package controllers

import javax.inject._
import play.api.mvc._
import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.ws
import play.libs.ws.WSClient
import models.LoginModel._
import play.api.db.Database
import play.api.Configuration

import controllers.models.DBapi._

/**
 * This is controller of the main page of CodeForge
 */

@Singleton
class HomeController @Inject()(cc: ControllerComponents,
                               db: Database,
                               config: Configuration) extends AbstractController(cc) {

  def basicDash= Action {
    implicit request =>

      val postVals = request.body.asFormUrlEncoded

      postVals.map {
        args =>
          val host: String = args("host").head
          val port: String = args("port").head
          val dbuser: String = args("user").head
          val dbpass: String = args("password").head
          //val dbtype: String = args(")

          //username.map{ user =>
          println(host, port, dbuser)

          val tester = new DBRequest(db, config)
          tester.testConnection()

      }
        Ok(views.html.dashboard("DashBoard"))

    //}.getOrElse(Redirect(routes.LoginController.login()))


   }

}
