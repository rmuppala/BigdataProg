import org.apache.spark.sql.SparkSession

object SparkCharCount {

  def main(args: Array[String]) {



    System.setProperty("hadoop.home.dir", "C:\\Users\\mraje\\Documents\\UMKC\\KDM\\winutils")

    val sc = SparkSession
      .builder
      .appName("SparkCharCount")
      .master("local[*]")
      .getOrCreate().sparkContext


    val input= sc.textFile("input")

    val wc=input.flatMap(line=>{line.split("")}).map(char=>(char,1)).cache()

    val output=wc.reduceByKey(_+_)

//    output.saveAsTextFile("output")

    val o=output.collect()

    print("Words:Count \n")
    o.foreach{case(word,count)=>{

      print(word+" : "+count+"\n")

    }}



  }

}
