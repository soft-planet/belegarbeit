import javassist.bytecode.SignatureAttribute.ArrayType
import org.apache.spark
import org.apache.spark.sql.{SparkSession, types}
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.{ArrayType, StringType, StructField, StructType}

object ReadCsv {
  def main(args: Array[String]) {

    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("Belegarbeit")
      .getOrCreate();



    val path = "src/main/resources/testSample.json"
    val df = spark.read.option("multiline", "true").json(path).toDF()
    df.show();




  }
}
