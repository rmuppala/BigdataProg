import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object WorldcupRDDDFF {

  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "C:\\Users\\mraje\\Documents\\UMKC\\KDM\\winutils")
    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .config("spark.master", "local")
      .getOrCreate()


    val wc = spark.sparkContext.textFile("data\\WorldCups.csv")


    val df2 = spark.read.format("csv").option("header", "true").load("data\\WorldCups.csv")
    df2.createOrReplaceTempView("worldcups")


    //select Year ,Country and Goalsscored using RDD
    val wcfileds = wc.map(line => {
      val fd = line.split(",")
      (fd(0), fd(1), fd(6))
    })

    wcfileds.foreach( x=> println(x._1 + "," + x._2 + "," + x._3))

    //select Year ,Country and Goalsscored using DFF
    val y = spark.sql("select Year, Country, GoalsScored From worldcups ")
    y.show()

    //RDD get Year Country GoalsScored > 100
    val h = wcfileds.first()
    val wcfileds1  = wcfileds.filter(row => row != h)
    wcfileds1.filter(  _._3.toInt > 100 ).foreach(fd => {

     // println(fd._1 + "," + fd._2 + " ," + fd._3)
    })

    //filter by GoalsScored>100 DFF
    import spark.sqlContext.implicits._
    val y1 = y.filter($"GoalsScored" > 100)
    //y1.show()

    //How many times each country hosted World Cup using RDD
    println(wcfileds1.groupBy(_._2).foreach( x=> println (x._1 + "," + x._2.size) ))


    //How many times each country hosted World Cup DFF
    y.groupBy("Country").count().show()


    //Average goals scored using RDD
    val avgValue = wcfileds1.map(_._3.toInt).sum() / wcfileds1.count()
    println("Average = " + avgValue.round )

    //Average goals scored using RDD
    val Avg_goals = spark.sql("SELECT ROUND(AVG(GoalsScored),0) AS average_goals FROM worldcups ")
    Avg_goals.show()

    //Years USA hosted world cup RDD
    val yu = wcfileds1.filter(_._2.equals("USA"))
    yu.foreach(fd => {
      println(fd._1 + "," + fd._2 + " ," + fd._3)
    })

    //Years USA hosted world cup DFF
    y.filter(($"Country" === "USA")).show()

  }
}
