import LanguageDetection.getClass
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object StopwordFilter {
def filter(dirty:RDD[(String)],lang:String,sc:SparkContext):RDD[(String)]={

  val txtFiles=Map(
    "DE"->"stop-words-german.txt",
    "ENG"->"stopwords-eng.txt",
    "UNKOWN"->"empty.txt")

  val file = getClass.getResource("/"+txtFiles(lang)).getFile
  val stopwords =sc.textFile(file)
  val stopwordsRDD=stopwords.flatMap(line =>line.split("\n"))

  dirty.subtract(stopwordsRDD)
}
}
