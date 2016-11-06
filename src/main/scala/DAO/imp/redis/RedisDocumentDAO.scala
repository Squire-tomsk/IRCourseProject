package DAO.imp.redis

import DAO.BasicDAO
import DAO.traits.DocumentDAO

/**
  * Created by abuca on 04.11.16.
  */
class RedisDocumentDAO extends DocumentDAO {
  val docKeyPrefix: String = "document:docId:"

  override def setDocument(docId: Long, text: String, url: String) = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.set(docKeyPrefix + docId, text)
        client.set(docKeyPrefix + docId + ":url", text)
      }
    }
  }

  override def getDocument(docId: Long): String = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.get(docKeyPrefix + docId).getOrElse("")
      }
    }
  }


  override def getUrl(docId: Long): String = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.get(docKeyPrefix + docId + ":url").getOrElse("")
      }
    }
  }

  override def getStoredDocumentCount: Long = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        val keys = client.keys(docKeyPrefix + "*").getOrElse(List())
        keys.length.toLong
      }
    }
  }

  override def erace() = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        val keys = client.keys(docKeyPrefix + "*").getOrElse(List())
          .map({ key => key.getOrElse("") })
          .foreach(key => client.del(key))
      }
    }
  }
}
