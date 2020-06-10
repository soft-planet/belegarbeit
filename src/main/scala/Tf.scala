import org.apache.spark.rdd.RDD

object Tf {
  def tf(tokens:RDD[(String)]):RDD[(String,Int)]={
    tokens.map(token => (token,1)).reduceByKey(_+_)
  }

}
