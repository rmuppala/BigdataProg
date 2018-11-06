import org.apache.spark.sql.SparkSession
import java.io.FileWriter
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import java.io.FileOutputStream
import java.io.File
import java.io.BufferedWriter
import java.io.PrintWriter

object LogfileGeneration {

  def main(args: Array[String]): Unit = {

    System.setProperty("hadoop.home.dir", "C:\\Users\\mraje\\Documents\\UMKC\\KDM\\winutils")

    val sc = SparkSession
      .builder
      .appName("LogfileGeneration")
      .master("local[*]")
      .getOrCreate().sparkContext


    val input = sc.textFile("data\\lorem.txt")


    // while (true)
    {


      //val bw = new BufferedWriter(new FileWriter(new File("data//logfile1.log"), true))

      import java.io._
      //val pb = new PrintWriter(new File("output/bionlpresult.txt" ))
      val bw = new PrintWriter(new File("data//logfile.log" ))

      while (true) {
        input.collect().foreach(line => {
          //print( line + "\n")
          val date = new Date()
          val sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSS")
          val dd = sdf.format(date)
          bw.write(dd + ":" + line.toString + "\n")
          bw.flush()
        })
      }
      bw.close()




    }


  }

}
