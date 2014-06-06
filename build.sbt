import AssemblyKeys._

assemblySettings

name := "hello-spark"

version := "1.0"

scalaVersion := "2.10.4"

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-core" % "1.0.0" % "provided",
	"org.apache.hadoop" % "hadoop-client" % "2.4.0" % "provided" excludeAll(
		ExclusionRule(organization = "org.jboss.netty"),
		ExclusionRule(organization = "io.netty"),
  		ExclusionRule(organization = "org.eclipse.jetty"),
  		ExclusionRule(organization = "org.mortbay.jetty"),
  		ExclusionRule(organization = "org.ow2.asm"),
  		ExclusionRule(organization = "asm"),
  		ExclusionRule(organization = "commons-logging"),
  		ExclusionRule(organization = "org.slf4j"),
  		ExclusionRule(organization = "org.scala-lang", artifact = "scalap"),
  		ExclusionRule(organization = "org.apache.hadoop"),
  		ExclusionRule(organization = "org.apache.curator"),
  		ExclusionRule(organization = "org.powermock"),
  		ExclusionRule(organization = "it.unimi.dsi"),
  		ExclusionRule(organization = "org.jruby"),
  		ExclusionRule(organization = "org.apache.thrift"),
  		ExclusionRule(organization = "javax.servlet", artifact = "servlet-api")
	),
	"com.typesafe.play" %% "play-json" % "2.3.0"
)

run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))

runMain in Compile <<= Defaults.runMainTask(fullClasspath in Compile, runner in (Compile, run))