resolvers += Resolver.url(
  "bintray-metabookmarks",
  url("https://dl.bintray.com/metabookmarks/sbt-plugin-releases/")
)(Resolver.ivyStylePatterns)

addSbtPlugin("com.dwijnand"      % "sbt-travisci" % "1.1.1")
addSbtPlugin("org.wartremover"   % "sbt-wartremover" % "2.2.1")
addSbtPlugin("com.lucidchart"    % "sbt-scalafmt" % "1.12")
addSbtPlugin("com.typesafe.sbt"  % "sbt-git"      % "0.9.3")
addSbtPlugin("de.heikoseeberger" % "sbt-header"   % "3.0.2")

addSbtPlugin("io.metabookmarks" % "sbt-slick-plugin" % "0.1.0")

libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.25" // Needed by sbt-git



