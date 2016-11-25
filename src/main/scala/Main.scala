import DAO.BasicDAO
import DAO.traits.DocumentDAO
import engine.{SimpleSearchEngine, SimpleSearchEngineBenchmark}
import utils.{DataInitilizer, TermExtractor}

object Main {
  def main(args: Array[String]) {
    BasicDAO.init()
    startBenchmark
    collectBashOrgData
  }

  def startBenchmark = {
    val initilizer = new DataInitilizer
    val bench = new SimpleSearchEngineBenchmark
    initilizer.initilizeNPLData
    bench.run
  }

  def collectWikipediaData = {
    val initilizer = new DataInitilizer
    initilizer.initilizeWikipediaData
  }

  def collectBashOrgData = {
    val initilizer = new DataInitilizer
    initilizer.initilizeBashOrgData
  }
}