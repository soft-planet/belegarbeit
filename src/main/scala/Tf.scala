import org.apache.spark.rdd.RDD

object Tf {
  def tf(tokens:Array[(String)]):Map[String,Int]={
    tokens.map(token => (token,1)).groupBy(x=>x._1).map(x=>(x._1,x._2.size))
  }

}
