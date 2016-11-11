
import DAO.BasicDAO
import DAO.imp.postgres.PostgresDocumentDAO
import DAO.imp.redis.BashOrgCrawlerDAO
import crawlers.BashOrgCrawler
import structures.Dictionary
import utils.TermExtractor

object Main {
  def main(args: Array[String]) {
    BasicDAO.init()

    fillDictionary
  }

  def crawlDocsFromBashOrg : Unit = {
    val documentDAO = new PostgresDocumentDAO
    val crawlerDAO = new BashOrgCrawlerDAO
    documentDAO.erace()
    crawlerDAO.erace()

    val crawler = new BashOrgCrawler
    crawler begin
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