package DAO.traits

import DAO.imp.redis.RedisDocumentDAO

/**
  * Created by abuca on 04.11.16.
  */
object DocumentDAO {
  var documentDAO: DocumentDAO = new RedisDocumentDAO
  def getDAO: DocumentDAO = documentDAO
}

trait DocumentDAO {
  def setDocument(docId: Long, text: String, url: String = "")

  def getDocument(docId: Long): String

  def getDocumentOrLink(docId: Long): String

  def getUrl(docId: Long): String

  def deleteDocument(docId: Long)

  def getStoredDocumentCount: Long

  def erace()
}
