package DAO

import DAO.imp.postgres.PostgresDocumentDAO
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by abuca on 07.11.16.
  */
class PostgresDocumentDAOSpec extends FlatSpec with Matchers{
  "The PostgresDocumentDAO" should "store document data associated with docID" in {
    BasicDAO.init()
    val documentDAO = new PostgresDocumentDAO
    documentDAO.deleteDocument(-1)
    documentDAO.setDocument(-1,"Test","Http:\\test_url.com")
    documentDAO.getDocument(-1) should be ("Test")
    documentDAO.getUrl(-1) should be ("Http:\\test_url.com")
    documentDAO.deleteDocument(-1)
    documentDAO.getDocument(-1) should be ("")
    documentDAO.getUrl(-1) should be ("")
  }
}
