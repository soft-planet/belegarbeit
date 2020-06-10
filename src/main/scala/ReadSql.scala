import javassist.bytecode.SignatureAttribute.ArrayType
import org.apache.spark
import org.apache.spark.sql.{SparkSession, types}
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.{ArrayType, StringType, StructField, StructType}
import java.sql.{Connection,DriverManager}

import org.apache.spark.ml.feature.{HashingTF,  Tokenizer}

object ReadSql {

  var connection:Connection=_
  def main(args: Array[String]) {

    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("Belegarbeit")
      .getOrCreate();


    val url = "jdbc:mysql://localhost/geniuscom?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"
    val driver = "com.mysql.jdbc.Driver"
    val username = "root"
    val password = ""

    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      val statement = connection.createStatement
      val rs = statement.executeQuery("SELECT content,url as docNr FROM responses LIMIT 100")
      while (rs.next) {
        val docNr = rs.getInt("docNr")
        val content = rs.getInt("content")
      }
    } catch {
      case e: Exception => e.printStackTrace
    }
    connection.close


  }
}
