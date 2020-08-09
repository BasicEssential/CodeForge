package controllers

import javax.inject._
import play.api.mvc._

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.ws
import play.libs.ws.WSClient

import models.LoginModel._

/**
 * This is controller of the main page of CodeForge
 */

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def basicDash = Action { implicit request =>

    Ok(views.html.dashboard("DashBoard"))
   }

}
