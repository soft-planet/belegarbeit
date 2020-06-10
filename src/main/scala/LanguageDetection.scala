import java.io.InputStream

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source



object LanguageDetection {


  def detect(tokens:RDD[(String)],sc: SparkContext):String={



    val fileDe = getClass.getResource("/top1000de.txt").getFile
    val wordsDe =sc.textFile(fileDe)
    val wordsRddDe=wordsDe.flatMap(line =>line.split("\n"))



    val fileEng = getClass.getResource("/google-10000-english.txt").getFile
    val wordsEng =sc.textFile(fileEng)
    val wordsRddEng=wordsEng.flatMap(line =>line.split("\n"))

    val tokensDistinct=tokens.distinct

    val intersectionDe=wordsRddDe.intersection(tokens).count()
    val intersectionEng=wordsRddEng.intersection(tokens).count()
    val scoring=Map("DE" -> intersectionDe,"ENG"->intersectionEng,"UNKNOWN"->1L)

    scoring.maxBy { case (key:String, value:Long) => value }._1
  }

}
