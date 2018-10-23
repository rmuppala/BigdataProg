
  import org.apache.spark.{SparkConf, SparkContext}

  object MergeSort {
    def main(args: Array[String]) {

      System.setProperty("hadoop.home.dir", "C:\\Users\\mraje\\Documents\\UMKC\\KDM\\winutils")

      val conf = new SparkConf()
        .setMaster("local")
        .setAppName("MergeSort")
        .setSparkHome("src/main/resources")
      val sc = new SparkContext(conf)


      /*val lstdata=sc.textFile("data/Lst")
      val txt = sc.textFile("data/Lst").map(line => line.split(","))
      val data1 = sc.parallelize(txt)
      data1.map(x => (x,1)) */


      val data = sc.parallelize(Array(38,27,43,3,9,82,10))

      val nlist = data.map(x => (x,1)).sortByKey()

      nlist.foreach(f => print(f._1 + " "))






    }


}
