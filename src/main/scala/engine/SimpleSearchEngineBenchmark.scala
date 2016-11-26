package engine

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import utils.NPLCollectionLoader

/**
  * Created by abuca on 25.11.16.
  */
class SimpleSearchEngineBenchmark {
  val benchmarkLogger = Logger(LoggerFactory.getLogger(this.getClass))
  val engine = new SimpleSearchEngine
  val dataLoader = new NPLCollectionLoader

  def run: Unit = {
    benchmarkLogger.info("[BENCHMARK] Benchmark started")
    benchmarkLogger.info("[BENCHMARK] Queries loading")
    val queryMap = dataLoader.loadQueries()
    benchmarkLogger.info("[BENCHMARK] Relevant sets loading")
    val relevantMap = dataLoader.loadRelevantSetMap()

    benchmarkLogger.info("[BENCHMARK] Searching for queries is started")
    val queryResults = queryMap.
      map(entety => {
        benchmarkLogger.info("[BENCHMARK] Search for query number " + entety._1)
        (entety._1, engine.search(entety._2))
      })

    benchmarkLogger.info("[BENCHMARK] MAP computation started")
    val meanAveragePrecition = queryResults.
      map(entety => {
        val aP = getAveragePrecition(relevantMap.get(entety._1).get, entety._2)
        benchmarkLogger.info("[BENCHMARK] AP for query number " + entety._1 + " is " + aP)
        aP
      }).
      reduce((aP1, aP2) => aP1 + aP2) / queryResults.size
    benchmarkLogger.info("[BENCHMARK] MAP computation finished. MAP: " + meanAveragePrecition)
    benchmarkLogger.info("[BENCHMARK] Benchmark finished")
  }

  def getAveragePrecition(relevantSet: Set[Long], searchResult: List[(Long, Double)]): Double = {
    (1 to searchResult.size + 1).
      map(n => getPrecition(relevantSet, searchResult.take(n).map(entety => entety._1).toSet)).
      reduce((precition1, precition2) => precition1 + precition2) / searchResult.size
  }

  def getPrecition(relevantSet: Set[Long], searchResult: Set[Long]): Double = {
    relevantSet.intersect(searchResult).size.toDouble / searchResult.size
  }
}
