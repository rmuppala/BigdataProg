object BFSDFS {


  class Graph[T] {

    type Vertex = T

    type GraphMap = Map[Vertex,List[Vertex]]

    var g:GraphMap = Map()



    def BFS(start: Vertex): List[List[Vertex]] = {



      def BFS0(elems: List[Vertex],visited: List[List[Vertex]]): List[List[Vertex]] = {

        val newNeighbors = elems.flatMap(g(_)).filterNot(visited.flatten.contains).distinct

        if (newNeighbors.isEmpty)

          visited

        else

          BFS0(newNeighbors, newNeighbors :: visited)

      }



      BFS0(List(start),List(List(start))).reverse

    }



    def DFS(start: Vertex): List[Vertex] = {



      def DFS0(v: Vertex, visited: List[Vertex]): List[Vertex] = {

        if (visited.contains(v))

          visited

        else {

          val neighbours:List[Vertex] = g(v) filterNot visited.contains

          neighbours.foldLeft(v :: visited)((b,a) => DFS0(a,b))

        }

      }

      DFS0(start,List()).reverse

    }

  }



  def main(args: Array[String]) {
    var intGraph = new Graph[Int]
   // 1 : 2,4
    //2 : 1,3
    //3 : 2,4
    //4 : 1,3
    //intGraph.g = Map(1 -> List(2, 4 ), 2 -> List(1, 3, 5 ), 3 -> List(2, 4), 4 -> List(1, 3) , 5 -> List(2,8) , 8 -> List(5))
    intGraph.g = Map(1 -> List(2, 5 ), 2 -> List(1, 3, 4 ), 3 -> List(2), 4 -> List(2) , 5 -> List(1,6,7) , 6 -> List(5)  , 7 -> List(5) )

    val bfsResult1 = intGraph.BFS(1)
    println("BFS 1")
    bfsResult1.foreach(w =>
    {
      w.foreach(x => print(x + " ") )
      println()
    })




    //println("BFS 2")
   /* val bfsResult2 = intGraph.BFS(2)
    bfsResult2.foreach(w =>
    {
      w.foreach(x => print(x + " ") )
      println()
    }) */


    println("DFS ")
    val dfsResult = intGraph.DFS(1)
    dfsResult.foreach(w =>
    {
      println(w)
    })
  }


}
