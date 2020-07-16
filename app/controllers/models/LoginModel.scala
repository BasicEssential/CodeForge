package controllers.models

import controllers.models.TaskListInMemoryModel.users

import scala.collection.mutable

object LoginModel {

  // create basic vars
  private val users = mutable.Map[String, String]()
  private val gits = mutable.Map[String, String]()
  private val databases = mutable.Map[String, String]()


  def validateUser(username: String, password: String): Boolean = {

    //TODO function to check DB whether user exist or not!

    users.get(username).map(_ == password).getOrElse(false)
  }





}
