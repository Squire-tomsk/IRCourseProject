package DAO.imp.redis

import DAO.BasicDAO
import SpellingCorrection.BiGrams

/**
  * Created by Anastasiia on 14.11.2016.
  */
class BiGramsListDAO{
  val postingListPrefix = "bigramlist:bigram:"

  def addTerm(term: String): Unit = {
    val bigramMaker = new BiGrams
    val bigrams = bigramMaker.make(term)
    BasicDAO.redisConnectionPool.withClient {
      client => {
        bigrams.foreach((bigram: String) => client.sadd(postingListPrefix + bigram, term))
      }
    }
  }

  def get(bigram: String): Set[String] ={
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.smembers(postingListPrefix + bigram)
          .getOrElse(Set())
          .map(word => word.get)
      }
    }
  }

  def erase(): Unit ={
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.keys(postingListPrefix + "*")
          .getOrElse(Set())
          .map({ key => key.getOrElse("") })
          .foreach(key => client.del(key))
      }
    }
  }
}
