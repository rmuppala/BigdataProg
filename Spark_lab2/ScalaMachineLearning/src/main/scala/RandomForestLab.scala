// scalastyle:off println
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.{SparkConf, SparkContext}
// $example on$
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.mllib.tree.model.RandomForestModel
import org.apache.spark.mllib.util.MLUtils
// $example off$

object RandomForestLab {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Samples").setMaster("local")
    val sc = new SparkContext(conf)

    // $example on$
    // Load and parse the data file.


    val abdata = sc.textFile("data/Absenteeism_at_work1.csv")

    //predict Absenteeism time in hours
    val DataSet = abdata.map { line =>
      val    columns = line.split(',')

      //println( line  + " columns length " + columns.length + " " + columns(0).toDouble)

      LabeledPoint(columns(19).toDouble,

        Vectors.dense(columns(0).toDouble, columns(1).toDouble, columns(2).toDouble, columns(3).toDouble, columns(4).toDouble, columns(5).toDouble,
          columns(6).toDouble, columns(7).toDouble, columns(8).toDouble, columns(9).toDouble,
          columns(10).toDouble, columns(11).toDouble, columns(12).toDouble, columns(13).toDouble,
          columns(14).toDouble, columns(15).toDouble, columns(16).toDouble, columns(17).toDouble,  columns(18).toDouble
        ))

    }
    val dl = DataSet.map(f => f.label).distinct()
    dl.foreach( f => println("Lable " + f))

    // Split the data into training and test sets (30% held out for testing)
    val splits = DataSet.randomSplit(Array(0.7, 0.3))
    val (trainingData, testData) = (splits(0), splits(1))

    // Train a RandomForest model.
    // Empty categoricalFeaturesInfo indicates all features are continuous.
    val numClasses = 3
    val categoricalFeaturesInfo = Map[Int, Int]()
    val numTrees = 3 // Use more in practice.
    val featureSubsetStrategy = "auto" // Let the algorithm choose.
    val impurity = "gini"
    val maxDepth = 4
    val maxBins = 32

    val model = RandomForest.trainClassifier(trainingData, numClasses, categoricalFeaturesInfo,
      numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins)

    // Evaluate model on test instances and compute test error
    val labelAndPreds = testData.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }


    val metrics = new MulticlassMetrics(labelAndPreds)


    val confusionMatrix = metrics.confusionMatrix
    println("Random Forest Confusion Matrix= \n",confusionMatrix)

    println( "Random Forest  Accuracy " + metrics.accuracy )
    println( "Random Forest precision for lable 0 " + metrics.precision(0.0) )
    println( "Random Forest fMeasure  for lable 0 " + metrics.fMeasure(0.0) )
    println( "Random Forest recall    for lable 0 " + metrics.recall(0.0) )

    println( "Random Forest precision for lable 1 " + metrics.precision(1.0) )
    println( "Random Forest fMeasure  for lable 1 " + metrics.fMeasure(1.0) )
    println( "Random Forest recall    for lable 1 " + metrics.recall(1.0) )

    println( "Random Forest precision for lable 2 " + metrics.precision(2.0) )
    println( "Random Forest fMeasure  for lable 2 " + metrics.fMeasure(2.0) )
    println( "Random Forest recall    for lable 2 " + metrics.recall(2.0) )

    val testErr = labelAndPreds.filter(r => r._1 != r._2).count.toDouble / testData.count()
    println(s"Test Error = $testErr")
   // println(s"Learned classification forest model:\n ${model.toDebugString}")

    // Save and load model
   // model.save(sc, "lab/RandomForestLab")
    //val sameModel = RandomForestModel.load(sc, "lab/RandomForestLab")
    // $example off$

    sc.stop()
  }
}
// scalastyle:on println