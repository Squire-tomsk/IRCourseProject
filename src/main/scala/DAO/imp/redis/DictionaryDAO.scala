package DAO.imp.redis

import DAO.BasicDAO

/**
  * Created by abuca on 10.11.16.
  */
class DictionaryDAO {
  val dictionaryKeyPrefix = "dictionary:word:"
  val dictionaryWordsKey = "dictionary:words"
  val dictionaryStopWordsKey = "dictionary:stopwords"

  def add(word: String,docID:Long): Unit ={
    BasicDAO.redisConnectionPool.withClient {
      client => {
        if(client.sismember(dictionaryWordsKey,word)){
          client.incr(dictionaryKeyPrefix+word+":freq")
        }
        else{
          client.sadd(dictionaryWordsKey,word)
          client.set(dictionaryKeyPrefix+word+":freq",1)
        }
        client.sadd(dictionaryKeyPrefix+word+":doclist",docID)
      }
    }
  }

  def exist(word: String): Boolean = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.sismember(dictionaryWordsKey,word)
      }
    }
  }

  def getFreq(word: String): Long ={
    BasicDAO.redisConnectionPool.withClient {
      client => {
        if(client.sismember(dictionaryWordsKey,word)){
          client.get(dictionaryKeyPrefix+word+":freq").getOrElse("0").toLong
        }
        else{
          0
        }
      }
    }
  }

  def getDocIDSet(word: String): Set[Long] = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.smembers(dictionaryKeyPrefix+word+":doclist")
          .getOrElse(Set())
          .map(docID => docID.getOrElse("-1").toLong)
          .filter(docID => docID > -1)
      }
    }
  }

  def addStopWord(stopWord: String): Unit = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.sadd(dictionaryStopWordsKey,stopWord)
      }
    }
  }

  def getStopWords() : Set[String] = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.smembers[String](dictionaryStopWordsKey).get.map(word => word.get)
      }
    }
  }

  def erase(): Unit ={
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.keys(dictionaryKeyPrefix + "*")
          .getOrElse(List())
          .map({ key => key.getOrElse("") })
          .foreach(key => client.del(key))
        client.del(dictionaryWordsKey)
        client.del(dictionaryStopWordsKey)
      }
    }
  }
}
