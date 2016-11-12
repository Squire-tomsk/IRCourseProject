
import DAO.BasicDAO
import DAO.imp.postgres.PostgresDocumentDAO
import DAO.imp.redis.BashOrgCrawlerDAO
import crawlers.BashOrgCrawler
import engine.SimpleSearchEngine
import structures.{Dictionary, PostingList}
import utils.{DocToVecConverter, TermExtractor}

object Main {
  def main(args: Array[String]) {
    BasicDAO.init()

    example
  }

  def example: Unit = {
    val engine = new SimpleSearchEngine
    val documentDAO = new PostgresDocumentDAO
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

  def crawlDocsFromBashOrg : Unit = {
    val documentDAO = new PostgresDocumentDAO
    val crawlerDAO = new BashOrgCrawlerDAO
    documentDAO.erace()
    crawlerDAO.erace()

    val crawler = new BashOrgCrawler
    crawler begin
  }

  def extractPostingLists : Unit = {
    val postingLists = new PostingList
    val extractor = new TermExtractor
    val documentDAO = new PostgresDocumentDAO

    val range = 1 to documentDAO.getStoredDocumentCount.toInt
    range.map(id => (id,documentDAO.getDocument(id))).
      map(doc => (doc._1,extractor.extract(doc._2))).
      foreach(doc => postingLists.add(doc._2,doc._1))
  }


  def fillDictionary(): Unit ={
    val extractor = new TermExtractor
    val documentDAO = new PostgresDocumentDAO
    val dictionary = new Dictionary
    dictionary.erace()

    val range = 1 to documentDAO.getStoredDocumentCount.toInt
    range.map(id => (id,documentDAO.getDocument(id))).
      map(doc => (doc._1,extractor.extract(doc._2))).
      foreach(doc => doc._2.foreach(term => {dictionary.add(term,doc._1)}))
  }
}