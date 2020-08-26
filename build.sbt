name := "CodeForge"
 
version := "1.0" 
      
lazy val `codeforge` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice,
                "com.typesafe.play" %% "play-slick" % "5.0.0",
                "com.typesafe.slick" %% "slick-codegen" % "3.3.2",
                "com.h2database" % "h2" % "1.4.197",
                "org.postgresql" % "postgresql" % "42.2.11",
                "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
                "org.mindrot" % "jbcrypt" % "0.4"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

      