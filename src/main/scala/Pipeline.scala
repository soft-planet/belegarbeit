import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Pipeline {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("appName").setMaster("local")
    val sc = new SparkContext(conf)

    val csvData = ReadCsv.readFile("testSample.csv", sc)
    val tokenized = csvData.map(rec => (rec._1, Tokenizer.tokenize(rec._2)))


    val detectLanguages = tokenized.map(rec=>(rec._1,rec._2,LanguageDetection.detect(rec._2)))

    val preferredLanguages = List("DE", "ENG")
    val targetData=detectLanguages.filter(preferredLanguages contains _._3)
    val cleanData=targetData.map(rec=>(rec._1,StopwordFilter.filter(rec._2,rec._3),rec._3))

    val stemmedData=cleanData.map(rec=>(
      rec._1,
      if(rec._3=="DE") rec._2.map(word=>GermanStemmer.stem(word))
      else rec._2.map(word=>EnglishStemmer.stem(word))
      ))


    val tf=stemmedData.map(rec=>(rec._1,Tf.tf(rec._2)))
    println("invert")
  

    inverted.foreach(x=>{
      println("word"+x._1)
      x._2.foreach(y=>println(y))
    })
    val invertedIndex=new InvertedIndex(tf.collect().toMap).saveToFile("invertedIndex.bin")
    println("saved to File")
    val invertedIndexFromFile=new InvertedIndex(Map.empty).loadFromFile("invertedIndex.bin")
    println("Load File")
    println("search")
    invertedIndexFromFile.search("oh").foreach(x=>println(x._1+""+x._2))
  }
}
