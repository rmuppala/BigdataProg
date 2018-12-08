import org.apache.spark.mllib.classification.NaiveBayes
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.sql.SparkSession

object NaiveBayesLab extends App {


  System.setProperty("hadoop.home.dir", "C:\\Users\\mraje\\Documents\\UMKC\\KDM\\winutils")
  val spark = SparkSession
    .builder()
    .appName("Spark SQL basic example")
    .config("spark.master", "local")
    .getOrCreate()

  val sc = spark.sparkContext

  val abdata = sc.textFile("data/Absenteeism_at_work1.csv")

  //predict Absenteeism time in hours
  val NaiveBayesDataSet = abdata.map { line =>
    val    columns = line.split(',')

    //println( line  + " columns length " + columns.length + " " + columns(0).toDouble)

    LabeledPoint(columns(19).toDouble,

      Vectors.dense(columns(0).toDouble, columns(1).toDouble, columns(2).toDouble, columns(3).toDouble, columns(4).toDouble, columns(5).toDouble,
        columns(6).toDouble, columns(7).toDouble, columns(8).toDouble, columns(9).toDouble,
        columns(10).toDouble, columns(11).toDouble, columns(12).toDouble, columns(13).toDouble,
        columns(14).toDouble, columns(15).toDouble, columns(16).toDouble, columns(17).toDouble,  columns(18).toDouble
      ))

  }

  println(" Total number of data vectors =",
    NaiveBayesDataSet.count())

  val distinctNaiveBayesData = NaiveBayesDataSet.distinct()
  println("Distinct number of data vectors = ",
    distinctNaiveBayesData.count())

  distinctNaiveBayesData.collect().take(10).foreach(println(_))

  val allDistinctData =

    distinctNaiveBayesData.randomSplit(Array(.70,.30),13L)

  val trainingDataSet = allDistinctData(0)

  val testingDataSet = allDistinctData(1)

  println("number of training data =",trainingDataSet.count())

  println("number of test data =",testingDataSet.count())

  val myNaiveBayesModel = NaiveBayes.train(trainingDataSet)

  val predictedClassification = testingDataSet.map( x =>
    (myNaiveBayesModel.predict(x.features), x.label))

  predictedClassification.collect().foreach(println(_))

  val metrics = new MulticlassMetrics(predictedClassification)

  val confusionMatrix = metrics.confusionMatrix
  println("NaiveBayes Confusion Matrix= \n",confusionMatrix)



  println( "NaiveBayes Accuracy " + metrics.accuracy )
  println( "NaiveBayes precision for lable 0 " + metrics.precision(0.0) )
  println( "NaiveBayes fMeasure  for lable 0 " + metrics.fMeasure(0.0) )
  println( "NaiveBayes recall    for lable 0 " + metrics.recall(0.0) )

  println( "NaiveBayes precision for lable 1 " + metrics.precision(1.0) )
  println( "NaiveBayes fMeasure  for lable 1 " + metrics.fMeasure(1.0) )
  println( "NaiveBayes recall    for lable 1 " + metrics.recall(1.0) )

  println( "NaiveBayes precision for lable 2 " + metrics.precision(2.0) )
  println( "NaiveBayes fMeasure  for lable 2 " + metrics.fMeasure(2.0) )
  println( "NaiveBayes recall    for lable 2 " + metrics.recall(2.0) )
  val myModelStat=Seq(metrics.precision,metrics.fMeasure,metrics.recall)

  //myModelStat.foreach(println(_))

}