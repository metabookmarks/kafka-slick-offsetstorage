resolvers += Resolver.url(
  "bintray-jug-montpellier",
  url("https://dl.bintray.com/jug-montpellier/sbt-plugin-releases/")
)(Resolver.ivyStylePatterns)

addSbtPlugin("com.dwijnand"      % "sbt-travisci" % "1.1.0")
addSbtPlugin("com.geirsson"      % "sbt-scalafmt" % "0.6.6")
addSbtPlugin("com.typesafe.sbt"  % "sbt-git"      % "0.9.2")
addSbtPlugin("de.heikoseeberger" % "sbt-header"   % "1.8.0")

addSbtPlugin("org.jug-montpellier" % "sbt-slick-plugin" % "0.0.6")

libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.25" // Needed by sbt-git



