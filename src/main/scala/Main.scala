
import java.io.{FileNotFoundException, IOException}

import DAO.BasicDAO
import DAO.imp.redis.{BashOrgCrawlerDAO, WikipediaCrawlerDAO}
import DAO.traits.DocumentDAO
import SpellingCorrection.NGramsCollection
import crawlers.{BashOrgCrawler, WikipediaCrawler}
import engine.SimpleSearchEngine
import structures.{Dictionary, PostingList}
import utils.TermExtractor

import scala.io.Source

object Main {
  def main(args: Array[String]) {
    BasicDAO.init()
  }

  def crawlDocsFromWikipedia : Unit = {
    val documentDAO = DocumentDAO.getDAO
    val crawlerDAO = new WikipediaCrawlerDAO
    documentDAO.erace()
    crawlerDAO.erace()

    val crawler = new WikipediaCrawler
    crawler begin
  }

  def fillDictionary(): Unit ={
    val extractor = new TermExtractor
    val documentDAO = DocumentDAO.getDAO
    val dictionary = new Dictionary
    dictionary.erace()


    try {
      for (stopWord <- Source.fromFile(getClass.getResource("/stopwords.txt").getPath).getLines()) {
        dictionary.addStopWord(stopWord)
      }
    } catch {
      case ex: FileNotFoundException => println("Couldn't find that file.")
      case ex: IOException => println("Had an IOException trying to read that file")
    }

    val stopWords = dictionary.getStopWords()

    val range = 1 to documentDAO.getStoredDocumentCount.toInt
    range.map(id => (id,documentDAO.getDocument(id))).
      map(doc => (doc._1,extractor.extract(doc._2))).
      foreach(doc => doc._2.
        filter(term => !stopWords.contains(term)).
        foreach(term => {dictionary.add(term,doc._1)}))

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

  def crawlDocsFromBashOrg : Unit = {
    val documentDAO = DocumentDAO.getDAO
    val crawlerDAO = new BashOrgCrawlerDAO
    documentDAO.erace()
    crawlerDAO.erace()

    val crawler = new BashOrgCrawler
    crawler begin
  }

  def extractPostingLists : Unit = {
    val postingLists = new PostingList
    val extractor = new TermExtractor
    val documentDAO = DocumentDAO.getDAO

    val range = 1 to documentDAO.getStoredDocumentCount.toInt
    range.map(id => (id,documentDAO.getDocument(id))).
      foreach(doc => postingLists.add(doc._2,doc._1))
  }

  def fillNGrammIndex: Unit ={
    val ngramsCollection = new NGramsCollection
    ngramsCollection.erase()
    ngramsCollection.create(getClass.getResource("/eng_words.txt").getPath)
  }
}