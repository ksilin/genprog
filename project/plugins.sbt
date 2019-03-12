addSbtPlugin("com.dwijnand"     % "sbt-travisci"        % "1.1.1")
addSbtPlugin("com.geirsson"     % "sbt-scalafmt"        % "1.5.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-git"             % "0.9.3")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.12")
addSbtPlugin("ch.epfl.scala"    % "sbt-scalafix"        % "0.9.1")

libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.25" // Needed by sbt-git