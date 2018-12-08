import NaiveBayesLab.abdatacsv
import org.apache.spark.mllib.classification.NaiveBayes
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.sql.SparkSession

object PreprocessData extends App {


  System.setProperty("hadoop.home.dir", "C:\\Users\\mraje\\Documents\\UMKC\\KDM\\winutils")
  val spark = SparkSession
    .builder()
    .appName("Spark SQL basic example")
    .config("spark.master", "local")
    .getOrCreate()

  val sc = spark.sparkContext


  val abdatacsv = sc.textFile("data/Absenteeism_at_work.csv")

  //remove the header

  val header = abdatacsv.first()
  val abdata = abdatacsv.filter(row => row != header)
  import java.io._
  val pw = new PrintWriter(new File("data/Absenteeism_at_work1.csv" ))


  //predict Absenteeism time in hours
  val NaiveBayesDataSet = abdata.collect().foreach( line => {
    val columns = line.split(';')

    var lable = 0
    pw.write(columns(1).toDouble + "," + columns(2).toDouble + "," + columns(3).toDouble + "," + columns(4).toDouble + "," + columns(5).toDouble + "," +
    columns(6).toDouble + "," +  columns(7).toDouble + "," + columns(8).toDouble
    + "," + columns(9).toDouble   + "," +  columns(10).toDouble  + "," + columns(11).toDouble
    + "," + columns(12).toDouble  + "," + columns(13).toDouble   + "," +  columns(14).toDouble + "," + columns(15).toDouble
    + "," + columns(16).toDouble  + "," + columns(17).toDouble  + "," +   columns(18).toDouble  + "," + columns(19).toDouble + "," )
    //println (columns(20))
    if ( columns(20).toDouble < 4 ) {
      lable = 0
      pw.write("0")
    }
    else if (columns(20).toDouble >= 4 && columns(20).toDouble < 40) {
      lable = 1
      pw.write("1")
    }
    else if (columns(20).toDouble >= 40) {
      lable = 2
      pw.write("2")
    }

    //println (columns(20)  + " " + lable)
    //pw.write(lable)
    pw.write("\n")

  })

  pw.close()




}