import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

class InvertedIndex(oldII:Map[String, Map[String,Int]],documentsSize:Int) {

  val ii:Map[String, Map[String,Int]]=oldII
  def size()=documentsSize


  def saveToFile(file:String):Unit={
    val oos = {
      new ObjectOutputStream(new FileOutputStream(file))
    }
    try {
      oos.writeObject((ii,size()))
    } finally {
      oos.close()
    }
  }

  def loadFromFile(file:String): InvertedIndex ={

    val ois = new ObjectInputStream(new FileInputStream(file))
    try {
      val rawIndex = ois.readObject.asInstanceOf[(Map[String, Map[String,Int]],Int)]
      new InvertedIndex(rawIndex._1,rawIndex._2)
    } finally {
      ois.close()
    }
  }

  def search(query:String):Map[String,Double]={
  val tokens=Tokenizer.tokenize(query)
  val detectedlanguage=LanguageDetection.detect(tokens)
  val filterdStopwords=StopwordFilter.filter(tokens,detectedlanguage)

    val stemmedTokens= if(detectedlanguage=="DE")filterdStopwords.map(GermanStemmer.stem) else filterdStopwords.map(EnglishStemmer.stem)

    /*ii.foreach(x=>{println("word:"+x._1)
   x._2.foreach(y=>println(y._2+" x docNr:"+y._1))
    })*/
    val tokenTF:Map[String,Map[String,Int]]=stemmedTokens .map(token=>(token,(ii(token)))).toMap
    val tokenTFDF=tokenTF.map(rec=>(rec._1,rec._2,rec._2.size))
    val tokenTFDFCorupusSize=tokenTFDF.map(rec=>(rec._2,rec._3,this.size()))
    val tf_idf=tokenTFDFCorupusSize.map(rec=>(rec._1,Math.log10(rec._3.toDouble/rec._2.toDouble)))



    tf_idf.flatMap(rec=>rec._1.map { case (k, v) => (k, v * rec._2) }).toMap
  }
}
