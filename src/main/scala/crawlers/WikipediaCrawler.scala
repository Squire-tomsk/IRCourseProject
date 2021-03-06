package crawlers

import java.util.concurrent.atomic.AtomicLong

import DAO.imp.redis.WikipediaCrawlerDAO
import DAO.traits.DocumentDAO
import com.gaocegege.scrala.core.common.response.impl.HttpResponse
import com.gaocegege.scrala.core.spider.impl.DefaultSpider
import com.typesafe.scalalogging.Logger
import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory

/**
  * Created by abuca on 04.11.16.
  */
object WikipediaCrawler {
  var requiredDocCount = 3000
}

class WikipediaCrawler extends DefaultSpider {
  val requiredDocCount = WikipediaCrawler.requiredDocCount

  val crawlerLogger = Logger(LoggerFactory.getLogger(this.getClass))
  val processLogger = Logger(LoggerFactory.getLogger("console_logger"))

  val crawlerDAO = new WikipediaCrawlerDAO
  val documentDAO = DocumentDAO.getDAO

  val startPage = "https://en.wikipedia.org/wiki/Earth"
  val pagePrefix = "https://en.wikipedia.org"

  override var workerCount: Int = 8
  var storedDocumentCount = new AtomicLong
  storedDocumentCount.set(documentDAO.getStoredDocumentCount)

  var linksPool = new java.util.concurrent.LinkedTransferQueue[String]()

  def startUrl: List[String] = List("https://en.wikipedia.org/wiki/Earth",
    "https://en.wikipedia.org/wiki/Life",
    "https://en.wikipedia.org/wiki/Human",
    "https://en.wikipedia.org/wiki/History_of_the_world",
    "https://en.wikipedia.org/wiki/Culture",
    "https://en.wikipedia.org/wiki/Language",
    "https://en.wikipedia.org/wiki/The_arts",
    "https://en.wikipedia.org/wiki/Science")

  override def parse(response: HttpResponse): Unit = {
    val code = response.getResponse.getStatusLine.getStatusCode
    if (code == 200) {
      val title: String = response.getContentParser().title()
      if (!crawlerDAO.isTitleCrawled(title)) {
        val docID = storedDocumentCount.incrementAndGet()
        processLogger.info(docID + " document processed now")
        crawlerDAO.addToCrawledSet(title)
        val links = (response getContentParser).
          select("p").
          select("a[href^=/wiki]").
          toArray[Element](Array[Element]()).
          map(node => node.attr("href")).
          map(link => pagePrefix + link)
        if (docID + linksPool.size() < requiredDocCount) {
          links.foreach(link => linksPool.put(link))
          links.foreach(link => crawlerDAO.addLink(link))
        }
        val text = (response getContentParser).
          select("p").
          toArray[Element](Array[Element]()).
          map(node => node.text()).
          reduce((text1, text2) => text1 + text2)
        val url = pagePrefix + "/wiki/" + title.replace(' ', '_').replaceFirst("_-_Wikipedia", "")
        documentDAO.setDocument(docID, text, url)
        crawlerLogger.info("Crawled article:" + title + " from url: " + url + " docId: " + docID)
        processLogger.info("Crawled article:" + title + " crawled: " + docID + " from " + requiredDocCount)
      }
    }
    if (!linksPool.isEmpty && storedDocumentCount.get() <= requiredDocCount) {
      request(linksPool.poll(), parse)
    }
  }
}
