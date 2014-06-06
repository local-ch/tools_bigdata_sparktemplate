package com.github.twoerth

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.SparkContext._
import play.api.libs.json.{Json, JsValue}
// import com.esotericsoftware.kryo.Kryo
// import org.apache.spark.serializer.KryoRegistrator

class Event( json: JsValue ) {
	val timestamp = ( json \ "timestamp" ).validate[String].getOrElse("invalid")
	val url = ( json \ "url" ).validate[String].getOrElse("invalid")
	val query = ( json \ "UA" ).validate[String].getOrElse("invalid")
	val ua = ( json \ "UA" ).validate[String].getOrElse("invalid")

	val bot = ua.contains("bot")
}

/*
class MMPath( path: String ) extends Serializable {
	val elements = path.split("/").slice(2,5)
	val uid = if( elements.size == 3 ) { elements(0) } else { "invalid" }
	val family = if( elements.size == 3 ) { elements(1) } else { "invalid" }
	val event = if( elements.size == 3 ) { elements(2) } else { "invalid" }

	override def toString() = "MMPath( uid -> " + uid + ", family -> " + family + ", event -> " + event 
}
*/

/*
class MyRegistrator extends KryoRegistrator {
  override def registerClasses(kryo: Kryo) {
    kryo.register(classOf[MMPath])
  }
}
*/

object Job extends App {
	val conf = new SparkConf()

	conf.setMaster("spark://10.182.1.141:7077")
//	conf.setMaster("local[6]")
		.setAppName("Scala Hello Spark")
		.setJars( Seq("target/scala-2.10/hello-spark-assembly-1.0.jar") )
		.set("spark.executor.memory", "1g")
//		.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//		.set("spark.kryo.registrator", "mypackage.MyRegistrator")

	val sc = new SparkContext(conf)

	try {
		val distFile = sc.textFile("hdfs://10.182.1.143/a.local.ch/2014/*/*/*/logger-hw-inx01.log")
		val requests = distFile.map( _.split("\t")(2) )
		val parsed_requests = requests.map( x => Json.parse( x ) )
		val urls = parsed_requests.map( json => new Event( json ) ).filter( _.bot ).map( event => (event.ua, 1) )
		val reduced = urls.reduceByKey(_+_)

		reduced.collect.foreach( println )

	} finally {
		// Clean up after ourselves no matter what happens...
		sc.stop()
	}
}