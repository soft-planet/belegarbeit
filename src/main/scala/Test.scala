import org.apache.spark.{SparkConf, SparkContext}

object Test {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("appName").setMaster("local")
    val sc=new SparkContext(conf)
    val text="Das sind ein paar Stopworte die völlig uninteressant sind."
    val textRDD= sc.parallelize(List(text))
    val tokens=Tokenizer.tokenize(textRDD)
    //Spracherkennung
    val language=LanguageDetection.detect(tokens,sc)
    println(language)
    //TF
    Tf.tf(tokens).foreach(x=>println(x._1+":"+x._2))
    //Stopworte filtern
    println("STOPWORTE FILTERN:")
    StopwordFilter.filter(tokens,language,sc).foreach(println)

   val ii= new InvertedIndex(Map.empty)
     .add("liebe",1,"doc1")
     .add("liebe",100,"doc2")
     .add("test",1,"doc2")

  println(ii.ii)
  }
}
