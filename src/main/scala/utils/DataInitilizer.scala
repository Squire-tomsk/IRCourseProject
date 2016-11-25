package utils

import java.io.{FileNotFoundException, IOException}

import DAO.imp.redis.{BashOrgCrawlerDAO, WikipediaCrawlerDAO}
import DAO.traits.DocumentDAO
import SpellingCorrection.NGramsCollection
import com.typesafe.scalalogging.Logger
import crawlers.{BashOrgCrawler, WikipediaCrawler}
import org.slf4j.LoggerFactory
import structures.{Dictionary, PostingList}

import scala.io.Source

/**
  * Created by abuca on 25.11.16.
  */
class DataInitilizer {
  val processLogger = Logger(LoggerFactory.getLogger("console_logger"))

  def initilizeBashOrgData: Unit ={
    processLogger.info("Initialisation started for Bash.org data")
    crawlDocsFromBashOrg
    initilizeStructures
  }

  def initilizeWikipediaData: Unit ={
    processLogger.info("Initialisation started for wikipedia data")
    crawlDocsFromWikipedia
    initilizeStructures
  }

  def initilizeNPLData: Unit ={
    processLogger.info("Initialisation started for NPLcollection data")
    loadDocsFromNPL
    initilizeStructures
  }

  def initilizeStructures: Unit ={
    processLogger.info("Dictionary building is started")
    fillDictionary
    processLogger.info("Posting list building is started")
    extractPostingLists
    processLogger.info("Ngramm index building is started")
    fillNGrammIndex
    processLogger.info("Data initialisation finished")
  }

  private def loadDocsFromNPL : Unit = {
    val loader = new NPLCollectionLoader
    loader.loadDocsToDAO()
  }

  private def crawlDocsFromWikipedia : Unit = {
    val documentDAO = DocumentDAO.getDAO
    val crawlerDAO = new WikipediaCrawlerDAO
    documentDAO.erace()
    crawlerDAO.erace()

    val crawler = new WikipediaCrawler
    crawler begin
  }

  private def crawlDocsFromBashOrg : Unit = {
    val documentDAO = DocumentDAO.getDAO
    val crawlerDAO = new BashOrgCrawlerDAO
    documentDAO.erace()
    crawlerDAO.erace()

    val crawler = new BashOrgCrawler
    crawler begin
  }

  private def fillDictionary(): Unit ={
    val extractor = new TermExtractor
    val documentDAO = DocumentDAO.getDAO
    val dictionary = new Dictionary
    dictionary.erace()

    processLogger.info("[DICT] Stop words list loading")
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
    range.par.map(id => (id,documentDAO.getDocument(id))).
      map(doc => {
        processLogger.info("[DICT] Words extraction for doc " + doc._1)
        (doc._1,extractor.extract(doc._2))
      }).
      foreach(doc => {
        processLogger.info("[DICT] Save word for doc " + doc._1 + " to database")
        doc._2.
          filter(term => !stopWords.contains(term)).
          foreach(term => {dictionary.add(term,doc._1)})
      })

  }

  private def extractPostingLists : Unit = {
    val postingLists = new PostingList
    val documentDAO = DocumentDAO.getDAO

    val range = 1 to documentDAO.getStoredDocumentCount.toInt
    range.par.map(id => {
      processLogger.info("[POST LIST] Posting list building for doc " + id)
      (id,documentDAO.getDocument(id))
    }).
      foreach(doc => {
        processLogger.info("[POST LIST]  Save posting list for doc " + doc._1 + " to database")
        postingLists.add(doc._2,doc._1)
      })
  }

  private def fillNGrammIndex: Unit ={
    val ngramsCollection = new NGramsCollection
    ngramsCollection.erase()
    ngramsCollection.create(getClass.getResource("/eng_words.txt").getPath)
  }
}
