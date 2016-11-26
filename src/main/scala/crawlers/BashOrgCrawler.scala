package crawlers

import java.util.concurrent.atomic.AtomicLong

import DAO.imp.redis.BashOrgCrawlerDAO
import DAO.traits.DocumentDAO
import com.gaocegege.scrala.core.common.response.impl.HttpResponse
import com.gaocegege.scrala.core.spider.impl.DefaultSpider
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

/**
  * Created by abuca on 04.11.16.
  */
class BashOrgCrawler extends DefaultSpider {
  val crawlerLogger = Logger(LoggerFactory.getLogger(this.getClass))
  val processLogger = Logger(LoggerFactory.getLogger("console_logger"))

  val crawlerDAO = new BashOrgCrawlerDAO
  val documentDAO = DocumentDAO.getDAO
  val quoteNumbers = crawlerDAO.getNotCraledDocumentNumbersList
  override var workerCount: Int = 8
  var storedDocumentCount = new AtomicLong
  storedDocumentCount.set(documentDAO.getStoredDocumentCount)

  def startUrl = quoteNumbers map { number => getQuoteLink(number) }

  def getQuoteLink(number: Long): String = {
    "http://bash.org/?" concat number.toString
  }

  override def parse(response: HttpResponse): Unit = {
    val code = response.getResponse.getStatusLine.getStatusCode
    if (code == 200) {
      val quoteNumber: String = (("\\d+".r) findFirstIn ((response getContentParser) title)) getOrElse ("-1")
      val quote: String = (response getContentParser) select ("p[class$=qt]") html()
      crawlerDAO.addCrawledDocumentNumber(quoteNumber.toInt)
      if (!quote.isEmpty) {
        documentDAO.setDocument(storedDocumentCount.incrementAndGet(), quote, getQuoteLink(quoteNumber.toLong))
        processLogger.info(quoteNumber +
          " of " +
          crawlerDAO.amountOfDocuments)
        crawlerLogger.info("Crawled from url: " +
          getQuoteLink(quoteNumber.toInt) +
          " data: " +
          quote)
      }
    }
  }
}
