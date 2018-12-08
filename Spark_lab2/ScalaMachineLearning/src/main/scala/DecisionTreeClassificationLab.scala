// scalastyle:off println
//package org.apache.spark.examples.mllib



import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.{SparkConf, SparkContext}
// $example on$
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.model.DecisionTreeModel
import org.apache.spark.mllib.util.MLUtils
// $example off$

object DecisionTreeClassificationLab {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Samples").setMaster("local")
    val sc = new SparkContext(conf)


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

    println(" Total number of data vectors =",
      DataSet.count())

    val distinctData = DataSet.distinct()
    println("Distinct number of data vectors = ",
      distinctData.count())

    distinctData.collect().take(10).foreach(println(_))

    val allDistinctData =

      distinctData.randomSplit(Array(.70,.30),13L)

    val trainingDataSet = allDistinctData(0)

    val testingDataSet = allDistinctData(1)


    // Train a DecisionTree model.
    //  Empty categoricalFeaturesInfo indicates all features are continuous.
    val numClasses = 3 //dl.count().toInt // 2
    println(" numClasses " + numClasses)
    val categoricalFeaturesInfo = Map[Int, Int]()
    val impurity = "gini"
    val maxDepth = 5
    val maxBins = 32

    val model = DecisionTree.trainClassifier(trainingDataSet, numClasses, categoricalFeaturesInfo,
      impurity, maxDepth, maxBins)

    // Evaluate model on test instances and compute test error
    val labelAndPreds = testingDataSet.map { point =>
      val prediction = model.predict(point.features)
      ( prediction, point.label)
      //(point.label, prediction)
    }

    val metrics = new MulticlassMetrics(labelAndPreds)


    val confusionMatrix = metrics.confusionMatrix
    println("Decision Tree Confusion Matrix= \n",confusionMatrix)



    println( "Decision Tree  Accuracy " + metrics.accuracy )
    println( "Decision Tree precision for lable 0 " + metrics.precision(0.0) )
    println( "Decision Tree fMeasure  for lable 0 " + metrics.fMeasure(0.0) )
    println( "Decision Tree recall    for lable 0 " + metrics.recall(0.0) )

    println( "Decision Tree precision for lable 1 " + metrics.precision(1.0) )
    println( "Decision Tree fMeasure  for lable 1 " + metrics.fMeasure(1.0) )
    println( "Decision Tree recall    for lable 1 " + metrics.recall(1.0) )

    println( "Decision Tree precision for lable 2 " + metrics.precision(2.0) )
    println( "Decision Tree fMeasure  for lable 2 " + metrics.fMeasure(2.0) )
    println( "Decision Tree recall    for lable 2 " + metrics.recall(2.0) )

    val testErr = labelAndPreds.filter(r => r._1 != r._2).count().toDouble / testingDataSet.count()
    println(s"Test Error Decision Tree = $testErr")
   // println(s"Learned classification tree model:\n ${model.toDebugString}")

    // Save and load model
    //model.save(sc, "lab/DecisionTreeModelLab")
    val sameModel = DecisionTreeModel.load(sc, "lab/DecisionTreeModelLab")
    // $example off$

    sc.stop()
  }
}
// scalastyle:on println