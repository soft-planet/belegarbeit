import org.apache.spark.{SparkConf, SparkContext}

object Test {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("appName").setMaster("local")
    val sc=new SparkContext(conf)
    val text="ich spiele Fussball. Fussball macht spaß"
    val textRDD= sc.parallelize(List(text))
    val tokens=Tokenizer.tokenize(textRDD)
    //Spracherkennung
    println(LanguageDetection.detect(tokens,sc))
    //TF
    Tf.tf(tokens).foreach(x=>println(x._1+":"+x._2))
  }
}
