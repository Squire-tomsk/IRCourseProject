
import DAO.BasicDAO
import crawlers.BashOrgCrawler

object Main {
  def main(args: Array[String]) {
    BasicDAO.init()
    // Uncoment to erace DB

    /*val crawlerDAO = new BashOrgCrawlerDAO
    crawlerDAO.erace()*/

    /*val documentDAO = new RedisDocumentDAO
    documentDAO.erace()*/

    /*val documentDAO = new PostgresDocumentDAO
    documentDAO.erace()*/

    val crawler = new BashOrgCrawler
    crawler begin
  }
}