name := """style-services"""

version := "0.3.8"


lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  filters
)

/**********************************************************************************************/

/**
 *************************** Library Dependencies ********************************************
  *
 */


libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.4" withSources() withJavadoc()

libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1205-jdbc42" withSources() withJavadoc()

libraryDependencies += "com.h2database" % "h2" % "1.4.188" withSources() withJavadoc()

libraryDependencies += "org.mybatis" % "mybatis" % "3.4.0" withSources() withJavadoc()

libraryDependencies += "org.mybatis" % "mybatis-guice" % "3.7.1" withSources() withJavadoc()

libraryDependencies += "com.google.inject.extensions" % "guice-multibindings" % "4.0" withSources() withJavadoc()

libraryDependencies += "commons-dbcp" % "commons-dbcp" % "1.4"

libraryDependencies += "com.github.mumoshu" %% "play2-memcached-play24" % "0.7.0" withSources() withJavadoc()


libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.4.1" withSources() withJavadoc()

libraryDependencies += "com.typesafe.akka" % "akka-kernel_2.11" % "2.4.1" withSources() withJavadoc()

libraryDependencies += "com.typesafe.akka" % "akka-slf4j_2.11" % "2.4.1" withSources() withJavadoc()

libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.4.1" withSources() withJavadoc()

libraryDependencies += "commons-codec" % "commons-codec" % "1.10"

libraryDependencies += "redis.clients" % "jedis" % "2.8.1"

//Code Review
//libraryDependencies += "com.puppycrawl.tools" % "checkstyle" % "6.7" withSources() withJavadoc()
//
//libraryDependencies += "net.sourceforge.pmd" % "pmd" % "5.0.0" withSources() withJavadoc()

//Test
//libraryDependencies += "org.mockito" % "mockito-core" % "2.0.13-beta" % "test" withSources() withJavadoc()
//
//dependencyOverrides += "junit" % "junit" % "4.12" % "test" withSources() withJavadoc()

/** ******************************************************************************************/

/**
 *************************** resolvers ****************************************************
 */
resolvers ++= Seq(
  "Apache" at "https://repo1.maven.org/maven2/"
)
// 检查代码中使用的过时类细节
javacOptions += "-Xlint:deprecation"
javacOptions += "-Xlint:unchecked"
//// "Add mapper xml files to classpath" -- blank line necessary for SBT
//unmanagedResourceDirectories in Compile <+= baseDirectory(_ / "app")
//
//// but filter out java and html files that would then also be copied to the classpath
//excludeFilter in Compile in unmanagedResources :=  "*.java" || "*.html"||"*.scala"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

