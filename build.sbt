// *****************************************************************************
// Projects
// *****************************************************************************

lazy val genprog =
  project
    .in(file("."))
    .enablePlugins(GitVersioning)
    .settings(settings)
    .settings(
      libraryDependencies ++= Seq(
        library.helisa,
//        library.scalaChart,
        library.scalaCheck % Test,
        library.scalaTest  % Test
      )
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val helisa     = "0.8.0"
//      val scalaChart = "0.5.1"
      val scalaCheck = "1.14.0"
      val scalaTest  = "3.0.5"
    }
    val helisa     = "com.softwaremill"         %% "helisa"      % Version.helisa
//    val scalaChart = "com.github.wookietreiber" %% "scala-chart" % Version.scalaChart
    val scalaCheck = "org.scalacheck"           %% "scalacheck"  % Version.scalaCheck
    val scalaTest  = "org.scalatest"            %% "scalatest"   % Version.scalaTest
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
commonSettings ++
gitSettings

lazy val commonSettings =
  Seq(
    scalaVersion := "2.12.8",
    organization := "default",
    organizationName := "ksilin",
    startYear := Some(2017),
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-language:_",
      "-target:jvm-1.8",
      "-encoding",
      "UTF-8"
    ),
    unmanagedSourceDirectories.in(Compile) := Seq(scalaSource.in(Compile).value),
    unmanagedSourceDirectories.in(Test) := Seq(scalaSource.in(Test).value),
    shellPrompt in ThisBuild := { state =>
      val project = Project.extract(state).currentRef.project
      s"[$project]> "
    }
  )

lazy val gitSettings =
  Seq(
    git.useGitDescribe := true
  )
