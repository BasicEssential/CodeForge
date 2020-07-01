package controllers

import javax.inject._
import play.api.mvc._


@Singleton
class ToDoListProject @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def toDoList = Action {
    Ok("TODO")
  }

}