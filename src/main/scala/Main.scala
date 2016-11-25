
import java.io.{FileNotFoundException, IOException}

import DAO.BasicDAO
import DAO.imp.redis.{BashOrgCrawlerDAO, WikipediaCrawlerDAO}
import DAO.traits.DocumentDAO
import SpellingCorrection.NGramsCollection
import crawlers.{BashOrgCrawler, WikipediaCrawler}
import engine.{SimpleSearchEngine, SimpleSearchEngineBenchmark}
import structures.{Dictionary, PostingList}
import utils.{DataInitilizer, NPLCollectionLoader, TermExtractor}

import scala.io.Source

object Main {
  def main(args: Array[String]) {
    BasicDAO.init()
    val bench = new SimpleSearchEngineBenchmark
    bench run
  }

  def example: Unit = {
    val engine = new SimpleSearchEngine
    val documentDAO = DocumentDAO.getDAO
    val extractor = new TermExtractor
    val random = scala.util.Random

    val docID = math.abs(random.nextLong()) % documentDAO.getStoredDocumentCount
    val doc = documentDAO.getDocument(docID)

    val terms = extractor.extract(doc)
    val query = (1 to 3).
      map(i => random.nextInt(terms.length-1)).
      map(n => terms.apply(n)).
      reduce((term1,term2) => term1 + " " + term2)

    println(docID)
    println(engine.search(query))
  }

}