package DAO.traits

import DAO.imp.postgres.PostgresDocumentDAO
import DAO.imp.redis.RedisDocumentDAO

/**
  * Created by abuca on 04.11.16.
  */
object DocumentDAO {
  def getDAO: DocumentDAO = new RedisDocumentDAO//new PostgresDocumentDAO
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
