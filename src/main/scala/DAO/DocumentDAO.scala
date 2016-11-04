package DAO

import com.redis._
/**
  * Created by abuca on 04.11.16.
  */
class DocumentDAO {
  val docKeyPrefix:String = "document:docId:"

  def setDocument(docId:Long,document:String) = {
    BasicDAO.clienPool.withClient {
      client => {
        client.set(docKeyPrefix+docId,document)
      }
    }
  }

  def getDocument(docId:Long):String = {
    BasicDAO.clienPool.withClient {
      client => {
        client.get(docKeyPrefix+docId).getOrElse("")
      }
    }
  }

  def getStoredDocumentCount:Long = {
    BasicDAO.clienPool.withClient {
      client => {
        val keys = client.keys(docKeyPrefix+"*").getOrElse(List())
        keys.length.toLong
      }
    }
  }

  def erace() = {
    BasicDAO.clienPool.withClient {
      client => {
        val keys = client.keys(docKeyPrefix+"*").getOrElse(List())
          .map({key => key.getOrElse("")})
          .foreach(key => client.del(key))
      }
    }
  }
}
