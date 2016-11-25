package engine

import utils.NPLCollectionLoader

import scala.collection.Searching.SearchResult

/**
  * Created by abuca on 25.11.16.
  */
class SimpleSearchEngineBenchmark {
  val engine = new SimpleSearchEngine
  val dataLoader = new NPLCollectionLoader

  def run : Unit = {
    val queryMap = dataLoader.loadQueries()
    val relevantMap = dataLoader.loadRelevantSetMap()

    val queryResults = queryMap.
      filter(entety => entety._1 < 5).
      map(entety => (entety._1,engine.search(entety._2)))

    val meanAveragePrecition = queryResults.
      map(entety => getAveragePrecition(relevantMap.get(entety._1).get,entety._2)).
      reduce((aP1, aP2) => aP1+aP2) / queryResults.size
  }

  def getAveragePrecition(relevantSet: Set[Long],searchResult: List[(Long,Double)]): Double ={
    (1 to searchResult.size+1).
      map(n => getPrecition(relevantSet,searchResult.take(n).map(entety => entety._1).toSet)).
      reduce((precition1,precition2) => precition1+precition2)/searchResult.size
  }

  def getPrecition(relevantSet: Set[Long], searchResult: Set[Long]): Double ={
    relevantSet.intersect(searchResult).size.toDouble / searchResult.size
  }
}
