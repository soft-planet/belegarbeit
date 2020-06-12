import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

class InvertedIndex(oldII:Map[String, Map[String,Int]]) {

  val ii:Map[String, Map[String,Int]]=oldII

  def add(term: String,frequency:Int, doc: String):InvertedIndex ={
    val termFreq:Map[String,Int]=Map(doc->frequency)
    val newMap:Map[String, Map[String,Int]]=Map(term->termFreq)

      if(ii.exists(old=>old._1==term)){
        val newTermFreq:Map[String,Int]=termFreq++ii(term)
       new InvertedIndex(ii++Map(term->newTermFreq) )

      }else{
        new InvertedIndex(ii++newMap)
      }

  }

  def saveToFile(file:String):Unit={
    val oos = {
      new ObjectOutputStream(new FileOutputStream(file))
    }
    try {
      oos.writeObject(ii)
    } finally {
      oos.close()
    }
  }

  def loadFromFile(file:String): InvertedIndex ={

    val ois = new ObjectInputStream(new FileInputStream(file))
    try {
      val rawIndex = ois.readObject.asInstanceOf[Map[String, Map[String,Int]]]
      new InvertedIndex(rawIndex)
    } finally {
      ois.close()
    }
  }

  def search(query:String):Map[String,Int]={
  val tokens=Tokenizer.tokenize(query)
  val detectedlanguage=LanguageDetection.detect(tokens)
  val filterdStopwords=StopwordFilter.filter(tokens,detectedlanguage)

    val stemmedTokens= if(detectedlanguage=="DE")filterdStopwords.map(GermanStemmer.stem) else filterdStopwords.map(EnglishStemmer.stem)
    stemmedTokens.foreach(println)
    /*ii.foreach(x=>{println("word:"+x._1)
   x._2.foreach(y=>println(y._2+" x docNr:"+y._1))
    })*/
    stemmedTokens.flatMap(token=>ii(token)).toMap

  }
}
