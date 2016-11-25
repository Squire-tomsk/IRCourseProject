package utils

import java.io.{FileNotFoundException, IOException}

import DAO.imp.redis.{BashOrgCrawlerDAO, WikipediaCrawlerDAO}
import DAO.traits.DocumentDAO
import SpellingCorrection.NGramsCollection
import crawlers.{BashOrgCrawler, WikipediaCrawler}
import structures.{Dictionary, PostingList}

import scala.io.Source

/**
  * Created by abuca on 25.11.16.
  */
class DataInitilizer {

  def initilizeBashOrgData: Unit ={
    crawlDocsFromBashOrg
    initilizeStructures
  }

  def initilizeWikipediaData: Unit ={
    crawlDocsFromWikipedia
    initilizeStructures
  }

  def initilizeNPLData: Unit ={
    loadDocsFromNPL
    initilizeStructures
  }

  private def initilizeStructures: Unit ={
    fillDictionary
    extractPostingLists
    fillNGrammIndex
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
      map(doc => (doc._1,extractor.extract(doc._2))).
      foreach(doc => doc._2.
        filter(term => !stopWords.contains(term)).
        foreach(term => {dictionary.add(term,doc._1)}))

  }

  private def extractPostingLists : Unit = {
    val postingLists = new PostingList
    val documentDAO = DocumentDAO.getDAO

    val range = 1 to documentDAO.getStoredDocumentCount.toInt
    range.par.map(id => (id,documentDAO.getDocument(id))).
      foreach(doc => postingLists.add(doc._2,doc._1))
  }

  private def fillNGrammIndex: Unit ={
    val ngramsCollection = new NGramsCollection
    ngramsCollection.erase()
    ngramsCollection.create(getClass.getResource("/eng_words.txt").getPath)
  }
}
