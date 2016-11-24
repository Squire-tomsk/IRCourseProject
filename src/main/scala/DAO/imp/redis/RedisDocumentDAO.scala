package DAO.imp.redis

import DAO.BasicDAO
import DAO.traits.DocumentDAO

/**
  * Created by abuca on 04.11.16.
  */
class RedisDocumentDAO extends DocumentDAO {
  val docKeyTextPrefix: String = "document:text:docId:"
  val docKeyUrlPrefix: String = "document:url:docId:"

  override def setDocument(docId: Long, text: String, url: String) = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.set(docKeyTextPrefix + docId, text)
        client.set(docKeyUrlPrefix + docId, url)
      }
    }
  }

  override def getDocument(docId: Long): String = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.get(docKeyTextPrefix + docId).getOrElse("")
      }
    }
  }

  override def getDocumentOrLink(docId: Long): String = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        val document = client.get(docKeyTextPrefix + docId).getOrElse("")
        if(document.length > 1000){
          "<a href=\""+getUrl(docId)+"\">"+getUrl(docId).substring(30)+"</a>"
        }
        else {
          document
        }
      }
    }
  }


  override def getUrl(docId: Long): String = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.get(docKeyUrlPrefix + docId).getOrElse("")
      }
    }
  }

  override def getStoredDocumentCount: Long = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        val keys = client.keys(docKeyTextPrefix + "*").getOrElse(List())
        keys.length.toLong
      }
    }
  }

  override def erace() = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.keys(docKeyTextPrefix + "*").getOrElse(List())
          .map({ key => key.getOrElse("") })
          .foreach(key => client.del(key))
        client.keys(docKeyUrlPrefix + "*").getOrElse(List())
          .map({ key => key.getOrElse("") })
          .foreach(key => client.del(key))
      }
    }
  }

  override def deleteDocument(docId: Long): Unit = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.keys(docKeyTextPrefix + docId).getOrElse(List())
          .map({ key => key.getOrElse("") })
          .foreach(key => client.del(key))
        client.keys(docKeyUrlPrefix + docId).getOrElse(List())
          .map({ key => key.getOrElse("") })
          .foreach(key => client.del(key))
      }
    }
  }
}
