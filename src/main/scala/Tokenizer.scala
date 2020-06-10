import javassist.bytecode.SignatureAttribute.ArrayType
import org.apache.spark
import org.apache.spark.sql.{SparkSession, types}
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.{ArrayType, StringType, StructField, StructType}
import java.sql.{Connection, DriverManager}

import org.apache.spark.api.java.JavaRDD
import org.apache.spark.rdd.{JdbcRDD, RDD}
import org.apache.spark.rdd.JdbcRDD
object Tokenizer {

  def tokenize(text:RDD[String]):RDD[(String)]= {

    val lines = text
    val tokens=lines.map(x=>x.split("[\\p{Punct}\\p{Blank}\\s]{1,}").map(upperToken=>upperToken.toLowerCase))
    tokens.flatMap(x=>x)

  }
}
