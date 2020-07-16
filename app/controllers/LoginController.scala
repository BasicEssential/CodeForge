package controllers


import controllers.models.LoginModel
import javax.inject._
import play.api.mvc._

@Singleton
class LoginController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  // login form | index page
  def login = Action { implicit request =>
    Ok(views.html.index())
  }

  //validate user / login and redirect to GIT settings or Statistic
  def validateLogin = Action{
    implicit request=>
      val postVals = request.body.asFormUrlEncoded
      postVals.map{
        args =>
          val username = args("username").head
          val password = args("password").head

          if (LoginModel.validateUser(username, password)){
            println("Redirect to SETTINGS")
            Redirect(routes.LoginController.login())
          }
          else {
            println("FAIL!")
            Redirect(routes.LoginController.login())
          }
      }.getOrElse(Redirect(routes.LoginController.login()))
  }


}
