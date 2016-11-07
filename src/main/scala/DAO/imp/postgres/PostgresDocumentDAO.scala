package DAO.imp.postgres

import DAO.traits.DocumentDAO
import scalikejdbc._

/**
  * Created by abuca on 06.11.16.
  */
class PostgresDocumentDAO extends DocumentDAO{
  implicit val session = AutoSession
  override def setDocument(docId: Long, text: String, url: String): Unit = {
    sql"INSERT INTO documents VALUES (${docId},${url},${text})".executeUpdate.apply()
  }

  override def getDocument(docId: Long): String = {
    sql"SELECT text FROM documents WHERE id = ${docId}".map(rs => rs.string("text")).first().apply().getOrElse("")
  }

  override def getUrl(docId: Long): String = {
    sql"SELECT url FROM documents WHERE id = ${docId}".map(rs => rs.string("url")).first().apply().getOrElse("")
  }

  override def getStoredDocumentCount: Long = {
    sql"SELECT COUNT(*) FROM documents".map(rs => rs.long(1)).first().apply().getOrElse(-1)
  }

  override def erace(): Unit = {
    sql"DELETE FROM documents".executeUpdate.apply()
  }

  override def deleteDocument(docId: Long): Unit = {
    sql"DELETE FROM documents WHERE id = ${docId}".executeUpdate.apply()
  }
}
