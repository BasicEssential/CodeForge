package controllers


import controllers.models.LoginModel
import controllers.models.GitAPI._


import javax.inject._
import play.api.mvc._
import play.libs.ws.WSClient

import scala.util.{Try,Success,Failure}

/**
 * This is controller of all login processes
 */

@Singleton
class LoginController @Inject()(ws: WSClient, cc: ControllerComponents) extends AbstractController(cc) {

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
            Redirect(routes.LoginController.setGit())
          }
          else {
            println("FAIL!")
            Redirect(routes.LoginController.login())
          }
      }.getOrElse(Redirect(routes.LoginController.login()))
  }

  //init git settings page
  def setGit = Action {
    implicit request =>
      Ok(views.html.setup(LoginModel.gits))
  }

  //get info about git and parse datea
  def setGitByUser = Action{
    implicit request =>
      val postVals = request.body.asFormUrlEncoded

      postVals.map{
        args =>
          val gitBase: String = args("platform").head
          val userName: String = args("username").head //Try(args("username").head)
          val repoName: String = args("repo").head

          //инициируем класс для работы с API
          val gAPI = new GitStrings(gitBase, userName, "", repoName)
          println(gAPI.testConnection)



      }

      Ok(views.html.setupDB())
  }

  def setGitByOrg = Action{
    implicit request =>   Ok(views.html.setupDB())
  }

  def setDB = Action{
    implicit request =>   Ok(views.html.setupDB())
  }



  def toGit = Action{
    implicit request =>
      Ok(views.html.setupDB())
    //val gitConnect = ws.url("https://api.github.com/users/nameartem/repos").get()
  }
}
