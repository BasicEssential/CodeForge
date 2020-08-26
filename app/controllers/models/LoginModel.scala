package controllers.models

import scala.concurrent.Future
import scala.collection.mutable



object LoginModel {


  // create basic vars
  private val users = mutable.Map[String, String]("user" -> "pas")
  val gits = List[List[String]](List("GitHub.com", "1"), List("GitLab.com", "0"), List("BitBucket.com", "0"))
  val databases = List[List[String]](List("H2", "1"), List("PostgreSQL", "1"), List("MySQL", "0"))


  def validateUser(username: String, password: String): Boolean = {

    //TODO function to check DB whether user exist or not!

    users.get(username).map(_ == password).getOrElse(false)
  }





}
