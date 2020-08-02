package controllers.models

import play.api.mvc.{AbstractController, ControllerComponents}

import scala.util.Try
import play.libs.ws.WSClient

object GitAPI {
  // base classes
  class GitStrings(val gitBase: String = "",
                   val userName: String = "",
                   val orgName: String = "",
                   val repoName: String = ""
                  )(ws: WSClient, cc: ControllerComponents) extends AbstractController(cc) {
    //class

    def testConnection = if (userName != "") { baseUserString } else {baseOrgString}

    def baseUserString = "https://api.%s/users/%s/%s".format(gitBase.toLowerCase(),
                                                             userName.toLowerCase(),
                                                             repoName.toLowerCase())

    def baseOrgString = "https://api.%s/orgs/%s/%s".format(gitBase.toLowerCase(),
                                                           orgName.toLowerCase(),
                                                           repoName.toLowerCase())

  }
}
