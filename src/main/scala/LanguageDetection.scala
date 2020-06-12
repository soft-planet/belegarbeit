import java.io.InputStream

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.{Codec, Source}



object LanguageDetection {


  def detect(tokens:Array[String]):String={


    val topLanguages=Map(
      "DE"->"top1000de.txt",
      "ENG"->"google-10000-english.txt",
      "PL"->"top1000pl.txt",
      "ESP"->"top1000esp.txt",
      "RU"->"top1000ru.txt",
      "PRT"->"top1000prt.txt",
      "GRC"->"top1000grc.txt")
    val tokensDistinct=tokens.toSet
    val scoring= topLanguages.map{

      t=>
        val wordsSource=getClass.getResourceAsStream(t._2)
        val wordsSet = scala.io.Source.fromInputStream( wordsSource )(Codec("ISO-8859-1")).getLines().toSet
        val score=wordsSet.intersect(tokensDistinct).size
        (t._1,score)

    }++Map("UNKNOWN"->(1+tokensDistinct.size/10))


    scoring.maxBy { case (key:String, value:Int) => value }._1
  }

}
