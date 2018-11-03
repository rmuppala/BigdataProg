import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql._

object Worldcup {

  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "C:\\winutils")
    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .config("spark.master", "local")
      .getOrCreate()





    val df1 = spark.read.format("csv").option("header", "true").load("data\\WorldCupMatches.csv")
    df1.createGlobalTempView("WorldCupMatches")
    //df1.show()
    val df2 = spark.read.format("csv").option("header", "true").load("data\\WorldCups.csv")
    df2.createOrReplaceTempView("worldcups")
    //df2.show()

    val df3 = spark.read.format("csv").option("header", "true").load("data\\WorldCupPlayers.csv")
    df3.createOrReplaceTempView("WorldCupPlayers")


    //q1 :players for 1934 world cup final match

    print("1934 word cup final match players")
    val players=  spark.sql("select wc.Winner, wp.* " +
      "from  WorldCups wc,  WorldCupPlayers wp , global_temp.WorldCupMatches wm " +
      " where wp.MatchID = wm.MatchID " +
      " and wp.RoundID = wm.RoundID " +
      "and (wc.Winner = wm.Home_Team_Name or wc.Winner = wm.Away_Team_Name) " +
      "and wc.Year = 1938 ");

    //players.show()

    //q2: Players who won  worldcups and played total number of games
    val finalteams1 =  spark.sql("select wc.*, wp.* " +
      "from  WorldCups wc,  WorldCupPlayers wp , global_temp.WorldCupMatches wm " +
      " where wp.MatchID = wm.MatchID " +
      " and wp.RoundID = wm.RoundID " +
      "and wc.Winner = wm.Home_Team_Name  " +
      "and wp.Team_Initials = wm.Home_Team_Initials"
    );
    //finalteams1.show()
    val finalteams2 = spark.sql("select wc.*, wp.* " +
      "from  WorldCups wc,  WorldCupPlayers wp , global_temp.WorldCupMatches wm " +
      " where wp.MatchID = wm.MatchID " +
      " and wp.RoundID = wm.RoundID " +
      "and wc.Winner = wm.Away_Team_Name  " +
      "and wp.Team_Initials = wm.Away_Team_Initials" );
    //finalteams2.show()
    val finalteams = finalteams1.union(finalteams2)
    finalteams.show()
    finalteams.groupBy("Player_Name").count().orderBy(desc("count")).show()
    finalteams.createOrReplaceTempView("finalteams")



    //q3(ok): sql query on dataframe
    //df1.createOrReplaceTempView("WorldCupMatches")

    // val sqlDF1 = spark.sql("SELECT Stage, Stadium, City from WorldCupMatches")
    //sqlDF1.show(20)

    df2.createOrReplaceTempView("worldcups")
    //val sqlDF2 = spark.sql("SELECT year, Country, Winner FROM worldcups")
    //sqlDF2.show()
    //val unionDF = sqlDF1.union(sqlDF2)
    //unionDF.show()



    // Register the DataFrame as a global temporary view

    //q4(ok)
    df1.createGlobalTempView("WorldCupMatches1")
    // Global temporary view is tied to a system preserved database `global_temp`
    spark.sql("SELECT Stage, Stadium, City, Attendance, MatchID FROM global_temp.WorldCupMatches1").show(20)


    // q1(select just one filed)
    //df1.select("Stadium").show()
    //q2:(group by)
    //df1.groupBy("City").count().show(5)



    //q7:
    val Pattern_reg = spark.sql("SELECT * FROM global_temp.WorldCupMatches WHERE Stage LIKE 'Final' AND Home_Team_Initials LIKE 'ENG'")
    //Pattern_reg.show()

    //q8:
    val Avg_goals = spark.sql("SELECT Home_Team_Name AS Team, ROUND(AVG(Home_Team_Goals),0) AS average_goals FROM global_temp.WorldCupMatches GROUP BY Home_TEam_Name")
    //Avg_goals.show()

    //q9: Find out counties which scored more than 100 goals order by year
    val y = spark.sql("SELECT Year, Country, GoalsScored From worldcups WHERE GoalsScored>100 order by Year")
    //y.show()

    //RDD


    val Goals = spark.sql("SELECT Stage,Stadium,City,Home_Team_Name FROM global_temp.WorldCupMatches WHERE Home_Team_Goals >6")
      //Goals.show()


  }
}
