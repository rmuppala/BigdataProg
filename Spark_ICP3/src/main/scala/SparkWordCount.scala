import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

object SparkWordCount {

  def main(args: Array[String]) {



    System.setProperty("hadoop.home.dir", "C:\\Users\\mraje\\Documents\\UMKC\\KDM\\winutils")

    val sc = SparkSession
      .builder
      .appName("SparkWordCount")
      .master("local[*]")
      .getOrCreate().sparkContext


    val input= sc.textFile("input")

    //val wc=input.flatMap(line=>{line.split(" ")}).map(word=>(word,1)).cache()

    val l=input.flatMap(line=>{line.split(" ")}).map(word=>word ).toString()



    //val l = "hello"

    val  t =  l.distinct.toCharArray.map(x=>(x,l.count(_==x)))

    t.foreach( x=>
     println(x._1 + " " + x._2))







  }

}
