// *****************************************************************************
// Projects
// *****************************************************************************

lazy val `kafka-slick-offsetstorage` =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin, GitVersioning, GitBranchPrompt)
    .settings(settings)
    .settings(
      libraryDependencies ++= library.slicks ++
        Seq(
          library.log4j,
          library.logback % Runtime,
          library.postgresql,
          library.slickCodeGen % Provided,
          library.zookeeper,
          library.kafka,
          library.scalaCheck % Test,
          library.scalaTest % Test
        )
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {

    object Version {
      val slf4j = "1.7.25"
      val logback = "1.2.3"
      val postgresql = "42.2.2"
      val slick = "3.2.3"
      val zookeeper = "3.4.11"
      val kafka = "0.20"
      val scalaCheck = "1.13.5"
      val scalaTest = "3.0.5"
    }

    val log4j = "org.slf4j" % "log4j-over-slf4j" % Version.slf4j
    val logback = "ch.qos.logback" % "logback-classic" % Version.logback

    val postgresql = "org.postgresql" % "postgresql" % Version.postgresql
    val slicks = Seq("slick", "slick-hikaricp").map("com.typesafe.slick" %% _ % Version.slick)
    val slickCodeGen = "com.typesafe.slick" %% "slick-codegen" % Version.slick
    val zookeeper = "org.apache.zookeeper" % "zookeeper" % Version.zookeeper exclude("log4j", "log4j") exclude("org.slf4j", "slf4j-log4j12")
    val kafka = "com.typesafe.akka" %% "akka-stream-kafka" % Version.kafka
    val scalaCheck = "org.scalacheck" %% "scalacheck" % Version.scalaCheck
    val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
    gitSettings ++
    scalafmtSettings


lazy val commonSettings =
  Seq(
    // scalaVersion from .travis.yml via sbt-travisci
    scalaVersion := "2.12.5",
    organization := "io.metabookmarks",
    startYear := Some(2017),
    licenses += ("Apache-2.0",
      url("http://www.apache.org/licenses/LICENSE-2.0")),
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-language:_",
      "-target:jvm-1.8",
      "-encoding", "UTF-8"
    ),
    unmanagedSourceDirectories.in(Compile) := Seq(scalaSource.in(Compile).value),
    unmanagedSourceDirectories.in(Test) := Seq(scalaSource.in(Test).value),
    shellPrompt in ThisBuild := { state =>
      val project = Project.extract(state).currentRef.project
      s"[$project]> "
    },
    wartremoverWarnings in (Compile, compile) ++= Warts.unsafe
)

lazy val gitSettings =
  Seq(
    git.useGitDescribe := true
  )

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true,
    scalafmtOnCompile.in(Sbt) := false,
    scalafmtVersion := "1.4.0"
  )
fork in Test := true
