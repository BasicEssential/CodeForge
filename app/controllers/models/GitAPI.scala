package controllers.models

import play.api.mvc.{AbstractController, ControllerComponents}

import scala.util.Try
import play.api.libs.json._
import play.api.libs.json.Reads._
import javax.inject._

import scala.concurrent.{Await, ExecutionContext, Future, duration}
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.concurrent.duration.Duration
import javax.inject.Inject
import play.api.libs.ws.{WSClient, WSRequest}


object GitAPI {

  //компаньён для определения Option (Null|None объектов) в строки
  trait Usr{
    implicit def stringToOption(s: String) = Some(s)

    implicit def integerToOption(i: Int) = Some(i)
  }

  // класс объекта - Пользователь
  case class User(id: Int,
                  login: String,
                  avatar_url: String,
                  url: String,
                  followers_url: String,
                  following_url: String,
                  subscriptions_url: String,
                  organizations_url: String,
                  repos_url: String,
                  events_url: String,
                  name: String,
                  company: Option[String],
                  location: Option[String],
                  email: Option[String],
                  bio: Option[String],
                  public_repos: Int,
                  public_gists: Int,
                  followers: Int,
                  following: Int,
                  created_at: String) extends Usr

  // класс объекта для парсинка всех repos
  // получен по адресу - /user/{name}/repos
  case class RepoList(id: Int,
                      name: String,
                      license: Option[String],
                      privateStatus: Boolean,
                      forkStatus: Boolean,
                      html_url: String,
                      url: String,
                      description: Option[String],
                      events_url: String,
                      issues_url: String,
                      branches_url: String,
                      assignees_url: String,
                      language_url: String,
                      forks: Int,
                      open_issues: Option[Int],
                      watchers: Option[Int],
                      watchers_count: Option[Int],
                      folks_count: Option[Int],
                      open_issue_count: Option[Int],
                      created_at: String) extends Usr

  // класс объекта одного репозитория
  case class Repo(id: Int,
                  name: String,
                  license: String,
                  privateStatus: Boolean,
                  forkStatus: Boolean,
                  ownerLogin: String,
                  parentRepoId: Option[Int],
                  parentRepoName: Option[String],
                  html_url: String,
                  url: String,
                  description: Option[String],
                  events_url: String,
                  issues_url: String,
                  branches_url: String,
                  assignees_url: String,
                  language_url: String,
                  forks: Int,
                  open_issues: Option[Int],
                  watchers: Option[Int],
                  watchers_count: Option[Int],
                  folks_count: Option[Int],
                  //open_issue_count: Option[Int],
                  created_at: String) extends Usr

  case class Event(eventType: String,
                   actorId: Int,
                   actorLogin: String,
                   repoId: Int,
                   repoName: String,
                   created_ad: String
                  ) extends Usr
  /* =================================================== */
  class GitRequest @Inject()(wc: WSClient){

    // test connection to  Git
    def getStatus(path: String) = {
      implicit val context = scala.concurrent.ExecutionContext.Implicits.global

      wc.url(path).get().map{
         res => if (res.status == 200){println("Connection OK, 200!")
                                       "Connection successful"
                                       //Future.successful(("Connection successful"))
                                       }
                else {throw new Exception("!!!Does not connect!!!")}}
    }

    ////////////////////////////////////////////////////////////////////////
    // классы на чтение типов
    ///////////////////////////////////////////////////////////////////////

    //get json for parsing REPO
    def getResutlRepo(path: String) = {
      implicit val context = scala.concurrent.ExecutionContext.Implicits.global

      implicit val repoReads: Reads[Repo] = (
        (JsPath  \ "id").read[Int] and
        (JsPath  \ "name").read[String] and
        (JsPath  \ "license").read[String] and
        (JsPath  \ "privateStatus").read[Boolean] and
        (JsPath  \ "forkStatus").read[Boolean] and
        (JsPath  \ "ownerLogin").read[String] and
        (JsPath  \ "parentRepoId").readNullable[Int] and
        (JsPath  \ "parentRepoName").readNullable[String] and
        (JsPath  \ "html_url").read[String] and
        (JsPath  \ "url").read[String] and
        (JsPath  \ "description").readNullable[String] and
        (JsPath  \ "events_url").read[String] and
        (JsPath  \ "issues_url").read[String] and
        (JsPath  \ "branches_url").read[String] and
        (JsPath  \ "assignees_url").read[String] and
        (JsPath  \ "language_url").read[String] and
        (JsPath  \ "forks").read[Int] and
        (JsPath  \ "open_issues").readNullable[Int] and
        (JsPath  \ "watchers").readNullable[Int] and
        (JsPath  \ "watchers_count").readNullable[Int] and
        (JsPath  \ "folks_count").readNullable[Int] and
        //(JsPath  \ "open_issue_count").readNullable[Int] and
        (JsPath  \ "created_at").read[String]
        )(Repo.apply _)

      //: Future[JsResult[User]]
      val futureResult = wc.url(path).get().map {
        response =>
          response.json.validate[Repo] match {
            case JsSuccess(value, _) => {

              //делаем, что нужно
              println(value)
            }
            case e: JsError => println("Data does not available, check data type!")
          }
      }
    }

    //////////////////////////////////////////////////////////////////////////////////
    //get json for parsing REPOLIST
    def getResutlRepoList(path: String) = {
      implicit val context = scala.concurrent.ExecutionContext.Implicits.global

      implicit val repoReads: Reads[RepoList] = (
        (JsPath  \ "id").read[Int] and
          (JsPath  \ "name").read[String] and
          (JsPath  \ "license").readNullable[String] and
          (JsPath  \ "privateStatus").read[Boolean] and
          (JsPath  \ "forkStatus").read[Boolean] and
          (JsPath  \ "html_url").read[String] and
          (JsPath  \ "url").read[String] and
          (JsPath  \ "description").readNullable[String] and
          (JsPath  \ "events_url").read[String] and
          (JsPath  \ "issues_url").read[String] and
          (JsPath  \ "branches_url").read[String] and
          (JsPath  \ "assignees_url").read[String] and
          (JsPath  \ "language_url").read[String] and
          (JsPath  \ "forks").read[Int] and
          (JsPath  \ "open_issues").readNullable[Int] and
          (JsPath  \ "watchers").readNullable[Int] and
          (JsPath  \ "watchers_count").readNullable[Int] and
          (JsPath  \ "folks_count").readNullable[Int] and
          (JsPath  \ "open_issue_count").readNullable[Int] and
          (JsPath  \ "created_at").read[String]
        )(RepoList.apply _)




      //: Future[JsResult[User]]
      val futureResult = wc.url(path).get().map {
        response =>
          response.json.validate[RepoList] match {
            case JsSuccess(value, _) => {
              println(value)
            }
            case e: JsError => println("Data does not available, check data type!")
          }
      }
    }
    //////////////////////////////////////////////////////////////////////////////////
    //get json for parsing EVENT
    def getResutlEvent(path: String) = {
      implicit val context = scala.concurrent.ExecutionContext.Implicits.global

      implicit val repoReads: Reads[Event] = (
        (JsPath  \ "eventType").read[String] and
        (JsPath  \ "actorId").read[Int] and
        (JsPath  \ "actorLogin").read[String] and
        (JsPath  \ "repoId").read[Int] and
        (JsPath  \ "repoName").read[String] and
        (JsPath  \ "created_ad").read[String]
        )(Event.apply _)


      //: Future[JsResult[User]]
      val futureResult = wc.url(path).get().map {
        response =>
          response.json.validate[Event] match {
            case JsSuccess(value, _) => {

              //делаем, что нужно
              println(value)
            }
            case e: JsError => println("Data does not available, check data type!")
          }
      }
    }
    //////////////////////////////////////////////////////////////////////////////////
    //get json for parsing USER
    def getResutlUser(path: String) = {
      implicit val context = scala.concurrent.ExecutionContext.Implicits.global

      implicit val userReads: Reads[User] = (
        (JsPath  \ "id").read[Int] and
          (JsPath  \ "login").read[String] and
          (JsPath  \ "avatar_url").read[String] and
          (JsPath  \ "url").read[String] and
          (JsPath  \ "followers_url").read[String] and
          (JsPath  \ "following_url").read[String] and
          (JsPath  \ "subscriptions_url").read[String] and
          (JsPath \ "organizations_url").read[String] and
          (JsPath \ "repos_url").read[String] and
          (JsPath \ "events_url").read[String] and
          (JsPath \ "name").read[String] and
          (JsPath \ "company").readNullable[String] and
          (JsPath \ "location").readNullable[String] and
          (JsPath \ "email").readNullable[String] and
          (JsPath \ "bio").readNullable[String] and
          (JsPath \ "public_repos").read[Int] and
          (JsPath \ "public_gists").read[Int] and
          (JsPath \ "followers").read[Int] and
          (JsPath \ "following").read[Int] and
          (JsPath \ "created_at").read[String]
        )(User.apply _)

      //: Future[JsResult[User]]
      val futureResult = wc.url(path).get().map {
        response =>
          /* если надо в переменную
          User((response.json \ "id").as[Int],
            (response.json \ "login").as[String],
            (response.json \ "avatar_url").as[String],
            (response.json \ "url").as[String],
            (response.json \ "followers_url").as[String],
            (response.json \ "following_url").as[String],
            (response.json \ "subscriptions_url").as[String],
            (response.json \ "organizations_url").as[String],
            (response.json \ "repos_url").as[String],
            (response.json \ "events_url").as[String],
            (response.json \ "name").as[String],
            (response.json \ "company").asOpt[String],
            (response.json \ "location").asOpt[String],
            (response.json \ "email").asOpt[String],
            (response.json \ "bio").asOpt[String],
            (response.json \ "public_repos").as[Int],
            (response.json \ "public_gists").as[Int],
            (response.json \ "followers").as[Int],
            (response.json \ "following").as[Int],
            (response.json \ "created_at").as[String])
         */
          // так как уже все хорошо, то делаем через проверку типов
          response.json.validate[User] match {
            case JsSuccess(value, _) => {
              // из value можно доставать и записовать в БД
              println(value)
            }
            case e: JsError => println("Data does not available, check data type!")
          }
      }

    }






  }

  /* =================================================== */

  // base classes
  class GitStrings (val gitBase: String = "",
                    val userName: String = "",
                    val orgName: String = "",
                    val repoName: String = ""
                    ) {
    //val wc: WSClient
    implicit val context = scala.concurrent.ExecutionContext.Implicits.global

    //create test connection to API
    def testConnection = if (userName != "") {
      baseUserString
    } else {
      baseOrgString
    }

    //2 similar funcs for creating  API
    def baseUserString() = {
      if (repoName != "") {
        "https://api.%s/users/%s/%s".format(gitBase.toLowerCase(),
          userName.toLowerCase(),
          repoName.toLowerCase())
      }
      else {
        "https://api.%s/users/%s".format(gitBase.toLowerCase(),
          userName.toLowerCase())
      }
    }

    def baseOrgString() = {
      if (repoName != "") {
        "https://api.%s/users/%s/%s".format(gitBase.toLowerCase(),
          orgName.toLowerCase(),
          repoName.toLowerCase())
      }
      else {
        "https://api.%s/users/%s".format(gitBase.toLowerCase(),
          orgName.toLowerCase())
      }
    }


    //repos_url
    def parse(res: WSRequest) = {

//      val fRes =
//        res.get().map{
//          res =>
//            res.body
//        }
//
//      println(fRes, res.body)
      //val res: WSRequest = url(u)




    }

  }
}
