
lazy val `izumi-reflect` = project.in(file("."))
  .settings(
    scalacOptions ++= Seq(
        "-Ykind-projector",
        "-noindent",
        "-language:implicitConversions"
      ),
    crossScalaVersions := Seq("0.25.0-RC2"),
    scalaVersion := crossScalaVersions.value.head,
  )