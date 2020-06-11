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
}
