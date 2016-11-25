package utils

import DAO.traits.DocumentDAO

import scala.io.Source

/**
  * Created by abuca on 25.11.16.
  */
class NPLCollectionLoader {
  def loadDocsToDAO(): Unit ={
    val documentDAO = DocumentDAO.getDAO
    documentDAO.erace()
    val lineIterator = Source.fromFile(getClass.getResource("/NPL/doc-text").getPath).getLines()
    while (lineIterator.hasNext){
      var docId = lineIterator.next().toLong
      var docText = ""
      var nextLine = lineIterator.next()
      while (!nextLine.contains("/")){
        docText += nextLine
        nextLine = lineIterator.next()
      }
      documentDAO.setDocument(docId,docText,"http://127.0.0.1")
    }
  }

  def loadQueries(): Map[Int,String] ={
    var queryMap = Map[Int,String]()
    val lineIterator = Source.fromFile(getClass.getResource("/NPL/query-text").getPath).getLines()
    while (lineIterator.hasNext){
      val queryId = lineIterator.next().toInt
      var queryText = ""
      var nextLine = lineIterator.next()
      while (!nextLine.contains("/")){
        queryText += nextLine
        nextLine = lineIterator.next()
      }
      queryMap += (queryId -> queryText)
    }
    queryMap
  }

  def loadRelevantSetMap(): Map[Int,Set[Long]] ={
    var relevantMap = Map[Int,Set[Long]]()
    val lineIterator = Source.fromFile(getClass.getResource("/NPL/rlv-ass").getPath).getLines()
    while (lineIterator.hasNext){
      val queryId = lineIterator.next().toInt
      var rawRelevantDocIds = ""
      var nextLine = lineIterator.next()
      while (!nextLine.contains("/")){
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
    relevantMap
  }
}
