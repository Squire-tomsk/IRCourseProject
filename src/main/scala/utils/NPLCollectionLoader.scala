package utils

import DAO.traits.DocumentDAO
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.io.Source

/**
  * Created by abuca on 25.11.16.
  */
class NPLCollectionLoader {
  val loaderLogger = Logger(LoggerFactory.getLogger(this.getClass))
  val processLogger = Logger(LoggerFactory.getLogger("console_logger"))

  def loadDocsToDAO(): Unit = {
    processLogger.info("Document loading started")
    val documentDAO = DocumentDAO.getDAO
    documentDAO.erace()
    loaderLogger.info("Document DAO eraced")
    processLogger.info("Document DAO eraced")
    val lineIterator = Source.fromFile(getClass.getResource("/NPL/doc-text").getPath).getLines()
    while (lineIterator.hasNext) {
      var docId = lineIterator.next().toLong
      loaderLogger.info("Document " + docId + " loading")
      processLogger.info("Document " + docId + " loading")
      var docText = ""
      var nextLine = lineIterator.next()
      while (!nextLine.contains("/")) {
        docText += nextLine
        nextLine = lineIterator.next()
      }
      documentDAO.setDocument(docId, docText, "http://127.0.0.1")
    }
    processLogger.info("Document loading finished")
  }

  def loadQueries(): Map[Int, String] = {
    processLogger.info("Queries loading started")
    var queryMap = Map[Int, String]()
    val lineIterator = Source.fromFile(getClass.getResource("/NPL/query-text").getPath).getLines()
    while (lineIterator.hasNext) {
      val queryId = lineIterator.next().toInt
      loaderLogger.info("Query " + queryId + " loading")
      processLogger.info("Query " + queryId + " loading")
      var queryText = ""
      var nextLine = lineIterator.next()
      while (!nextLine.contains("/")) {
        queryText += nextLine
        nextLine = lineIterator.next()
      }
      queryMap += (queryId -> queryText)
    }
    processLogger.info("Queries loading finished")
    queryMap
  }

  def loadRelevantSetMap(): Map[Int, Set[Long]] = {
    var relevantMap = Map[Int, Set[Long]]()
    val lineIterator = Source.fromFile(getClass.getResource("/NPL/rlv-ass").getPath).getLines()
    processLogger.info("Relevant documents set loading started")
    while (lineIterator.hasNext) {
      val queryId = lineIterator.next().toInt
      processLogger.info("Relevant documents for query " + queryId + " loading")
      var rawRelevantDocIds = ""
      var nextLine = lineIterator.next()
      while (!nextLine.contains("/")) {
        rawRelevantDocIds += nextLine
        nextLine = lineIterator.next()
      }
      val parsedRelevantDocIds = rawRelevantDocIds.
        split(" ").
        filter(docID => !docID.isEmpty).
        map(docId => docId.toLong).
        toSet[Long]
      relevantMap += (queryId -> parsedRelevantDocIds)
    }
    processLogger.info("Relevant documents set loading finished")
    relevantMap
  }
}
