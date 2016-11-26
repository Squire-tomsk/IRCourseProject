import DAO.BasicDAO
import DAO.imp.postgres.PostgresDocumentDAO
import DAO.imp.redis.RedisDocumentDAO
import DAO.traits.DocumentDAO
import crawlers.WikipediaCrawler
import engine.SimpleSearchEngineBenchmark
import utils.DataInitilizer

object Main {
  def main(args: Array[String]) {
    BasicDAO.init()
    if(!args.isEmpty){
      if(args.length == 2){
        args(1) match {
          case "redis" => {
            WikipediaCrawler.requiredDocCount = 300
            DocumentDAO.documentDAO = new RedisDocumentDAO
          }
          case "postgres" => {
            WikipediaCrawler.requiredDocCount = 1000000
            DocumentDAO.documentDAO = new PostgresDocumentDAO
          }
          case _ => println("Wrong argument")
        }
      }
      args(0) match {
        case "bench" => startBenchmark
        case "bash.org" => collectBashOrgData
        case "wikipedia" => collectWikipediaData
        case "initialize" => initilizeStructures
        case _ => println("Wrong argument")
      }
    }
    else{
      println("Wrong arguments count")
    }
  }

  def startBenchmark = {
    val initilizer = new DataInitilizer
    val bench = new SimpleSearchEngineBenchmark
    initilizer.initilizeNPLData
    initilizer.initilizeStructures
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

  def initilizeStructures = {
    val initilizer = new DataInitilizer
    initilizer.initilizeStructures
  }
}