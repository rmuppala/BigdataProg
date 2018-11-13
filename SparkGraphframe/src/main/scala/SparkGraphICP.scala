import org.apache.spark.sql.SparkSession
import org.graphframes.GraphFrame

object SparkGraphICP {
  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "C:\\winutils")
    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .config("spark.master", "local")
      .getOrCreate()


    val input = spark.read.format("csv").option("header", "true").load("data/201508_station_data.csv")
    val output = spark.read.format("csv").option("header", "true").load("data/201508_trip_data.csv")


    val x = input.select("name","landmark").withColumnRenamed("name" ,"id").distinct()

    val y = output.select("Start_Station","End_Station", "Duration").distinct()
      .withColumnRenamed("Start_Station" ,"src")
      .withColumnRenamed("End_Station" ,"dst")
      .withColumnRenamed("Duration" ,"relationship")


    //x.show()
    y.show()


    val g = GraphFrame(x,y)
    //g.vertices.show()
    //g.edges.show()
    //g.inDegrees.show()
    //g.outDegrees.show()

   // g.degrees.show()

    //Search for pairs of vertices with edges in both directions between them.
    val motifs = g.find("(a)-[e]->(b); (b)-[e2]->(a)")
    val mfr = motifs.distinct()


    import java.io._
    //val pb = new PrintWriter(new File("output/bionlpresult.txt" ))
    val pw = new PrintWriter(new File("data/motifs.txt" ))
    mfr.collect().foreach(x => pw.write(x + "\n"))
    pw.close()

   // g.vertices.write.csv("data/ver")
    //g.edges.write.csv("data/edg")

  }
}
