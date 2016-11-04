
import DAO.{BashOrgCrawlerDAO, DocumentDAO}
import crawlers.BashOrgCrawler

object Main {
  def main(args: Array[String]) {
    // Uncoment to erace DB
    /*
    val crawlerDAO = new BashOrgCrawlerDAO
    val documentDAO = new DocumentDAO

    documentDAO.erace()
    crawlerDAO.erace()
    */

    val test = new BashOrgCrawler
    test begin
  }
}