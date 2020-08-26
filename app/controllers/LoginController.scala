package controllers


import controllers.models.LoginModel
import controllers.models.GitAPI._
import javax.inject._
import play.api.mvc._

import scala.concurrent.{Await, Future, duration}
import scala.concurrent.duration._
import play.api.libs.ws.{WSClient, WSRequest}
import play.api.db.{Database, NamedDatabase}
import play.api.http.HttpEntity
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.collection.mutable.ListBuffer


/**
 * This is controller of all login processes
 */

@Singleton
class LoginController @Inject()(ws:WSClient,
                                cc: ControllerComponents) extends AbstractController(cc) {

  // переменные для класса
  case class UserInfo(var userName: String,
                      var basicGitSpace: String,
                      var repoName: String)

  var userInfo: UserInfo = UserInfo("", "", "")

  // login form | index page
  def login = Action { implicit request =>

    // чтобы если уже был, то на dash закидывал
    //val username = request.session.get("username")

    //username.map{ user =>
    //  if (user.isEmpty) {
          Ok(views.html.index())
    //  }
    //  else {
    //    Ok(views.html.dashboard("DashBoard"))
    //  }
    //}.getOrElse(Redirect(routes.LoginController.login()))
  }

  def logout = Action {
    //сделать в html кнопку выхода (её функция
    Redirect(routes.LoginController.login()).withNewSession
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

            //сохраняем имя для дальнейшей работы (чтобы не обращаться)
            this.userInfo.userName = username
            println("Redirect to SETTINGS")
            Redirect(routes.LoginController.setGit()).withSession("username" -> username)
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
      Ok(views.html.setup(LoginModel.gits, this.userInfo.userName))
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
          val pathToAPI = gAPI.testConnection
          println(pathToAPI)

          val git = new GitRequest(ws)

          implicit val context = scala.concurrent.ExecutionContext.Implicits.global

          //соединение для тестирования
          // да, тормозим, но это надо сейчас, потом переделаем на пререлизе
          val conStatus: String = Await.result(git.getStatus(pathToAPI), duration.Duration.Inf)

          if (conStatus == "Connection successful"){
              //сохраняем параметры для настройки
              this.userInfo.basicGitSpace = userName
              this.userInfo.repoName = repoName

              // тестовый парсинг, потом убрать в функцию
              // перед выводом дашборда

            git.getResutlUser(pathToAPI)

              Redirect(routes.LoginController.setDB())

            } else {
            Redirect(routes.LoginController.login())
          }
          }.getOrElse(Redirect(routes.LoginController.login()))


      }

  def setGitByOrg = Action{
    implicit request =>   Ok(views.html.setupDB(this.userInfo.userName, this.userInfo.basicGitSpace, LoginModel.databases))
  }

  def setDB = Action{
    implicit request =>   Ok(views.html.setupDB(this.userInfo.userName, this.userInfo.basicGitSpace, LoginModel.databases))
  }



  def toGit = Action{
    implicit request =>
      Ok(views.html.setupDB(this.userInfo.userName, this.userInfo.basicGitSpace, LoginModel.databases))
    //val gitConnect = ws.url("https://api.github.com/users/nameartem/repos").get()
  }
}
