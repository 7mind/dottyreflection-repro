
lazy val `izumi-reflect` = project.in(file("."))
  .settings(
    scalacOptions ++= Seq(
        "-Ykind-projector",
        "-noindent",
        "-language:implicitConversions"
      ),
    crossScalaVersions := Seq("0.24.0-RC1"),
    scalaVersion := crossScalaVersions.value.head,
  )