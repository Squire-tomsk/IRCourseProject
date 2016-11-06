package DAO.traits

/**
  * Created by abuca on 04.11.16.
  */
trait DocumentDAO {

  def setDocument(docId: Long, text: String, url: String = "")

  def getDocument(docId: Long): String

  def getUrl(docId: Long): String

  def getStoredDocumentCount: Long

  def erace()
}
