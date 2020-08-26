package controllers.models

// database
//import play.api.Play.current
import javax.inject.Inject
import play.api.db.{Database, NamedDatabase}
import play.api.Configuration

import java.io.File
import com.typesafe.config.ConfigFactory
import play.api._

object DBapi {

  class DBRequest @Inject()(db: Database,
                            config: Configuration) {



    def testConnection() = {

        println(config.underlying.getString("db.default.driver"))

        println(config.underlying.getString("db.default.url"))


        //config.underlying.

        val conn = db.getConnection()

        try {
          val stmt = conn.createStatement
          val rs = stmt.executeQuery("SELECT '9!!!!!!!!!!!!' as testkey ")
          while (rs.next()) {
            println(rs.getString("testkey"))
          }
        }
        finally {
          conn.close()
        }
    }


  }


}
