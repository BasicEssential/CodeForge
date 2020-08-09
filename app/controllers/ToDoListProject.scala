package controllers

import controllers.models.TaskListInMemoryModel
import javax.inject._
import play.api.mvc._


@Singleton
class ToDoListProject @Inject()(cc: ControllerComponents) extends AbstractController(cc) {


  def login = Action{ implicit request =>
    Ok(views.html.login1())
  }

  def validateLogin(username: String, password: String) = Action{
    Ok(s"$username logged with password $password")
  }

  def validateLoginPost = Action{
    request=>
      val postVals = request.body.asFormUrlEncoded
      postVals.map{
        args =>
          val username = args("username").head
          val password = args("password").head

          if (TaskListInMemoryModel.validateUser(username, password)){
            Redirect(routes.ToDoListProject.toDoList()).withSession("username" -> username)
          }
          else {
            Redirect(routes.ToDoListProject.login()).flashing("error" -> "Invalid username or password")
          }
      }.getOrElse(Redirect(routes.ToDoListProject.login()))
  }

  def createUser = Action {
    request=>
      val postVals = request.body.asFormUrlEncoded
      postVals.map{
        args =>
          val username = args("username").head
          val password = args("password").head

          if (TaskListInMemoryModel.createUser(username, password)){
            Redirect(routes.ToDoListProject.toDoList()).withSession("username" -> username)
          }
          else {
            Redirect(routes.ToDoListProject.login()).flashing("error" -> "User creation failed")
          }
      }.getOrElse(Redirect(routes.ToDoListProject.login()))
  }

  def toDoList = Action {implicit
    request =>
      val usernameOption = request.session.get("username")
      usernameOption.map{username =>
          println(username)
          val todos = TaskListInMemoryModel.getTasks(username)
          Ok(views.html.toDoList(todos))
      }.getOrElse(Redirect(routes.ToDoListProject.login()))
  }

  def logOut = Action{
    Redirect(routes.ToDoListProject.login()).withNewSession
  }

}