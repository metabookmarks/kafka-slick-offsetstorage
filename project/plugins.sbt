resolvers += Resolver.url(
  "bintray-metabookmarks",
  url("https://dl.bintray.com/metabookmarks/sbt-plugin-releases/")
)(Resolver.ivyStylePatterns)

addSbtPlugin("com.dwijnand"      % "sbt-travisci" % "1.1.3")
addSbtPlugin("org.wartremover"   % "sbt-wartremover" % "2.3.7")
addSbtPlugin("com.lucidchart"    % "sbt-scalafmt" % "1.15")
addSbtPlugin("com.typesafe.sbt"  % "sbt-git"      % "1.0.0")
addSbtPlugin("de.heikoseeberger" % "sbt-header"   % "5.0.0")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.6")
addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.1")
addSbtPlugin("io.metabookmarks" % "sbt-slick-plugin" % "0.1.2")

libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.25" // Needed by sbt-git



