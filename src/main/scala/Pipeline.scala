import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Pipeline {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("appName").setMaster("local")
    val sc = new SparkContext(conf)

    val csvData = ReadCsv.readFile("testSample.csv", sc)
    val tokenized = csvData.map(rec => (rec._1, Tokenizer.tokenize(rec._2)))


    val detectLanguages = tokenized.map(rec=>(rec._1,rec._2,LanguageDetection.detect(rec._2)))
    detectLanguages.foreach(x=>println(x._3+" : "+x._2.mkString(" ")))

    val preferredLanguages = List("DE", "ENG")
    val targetData=detectLanguages.filter(preferredLanguages contains _._3)
    val filteredData=targetData.filter(rec=>preferredLanguages contains rec._3)
    val cleanData=filteredData.map(rec=>(rec._1,StopwordFilter.filter(rec._2,rec._3),rec._3))

    val stemmedData=cleanData.map(rec=>(
      rec._1,
      if(rec._3=="DE") rec._2.map(word=>GermanStemmer.stem(word))
      else rec._2.map(word=>EnglishStemmer.stem(word))
      ))

    val tf=stemmedData.map(rec=>(rec._1,Tf.tf(rec._2)))
    val invertedIndex=new InvertedIndex(tf.collect().toMap).saveToFile("invertedIndex.bin")
  }
}
