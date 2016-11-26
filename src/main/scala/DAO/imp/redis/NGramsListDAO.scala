package DAO.imp.redis

import DAO.BasicDAO
import SpellingCorrection.NGrams

/**
  * Created by Anastasiia on 14.11.2016.
  */
class NGramsListDAO {
  val ngrammListPrefix = "ngramlist:ngram:"

  def addTerm(term: String): Unit = {
    val ngramMaker = new NGrams
    val ngrams = ngramMaker.make(term)
    BasicDAO.redisConnectionPool.withClient {
      client => {
        ngrams.foreach((ngram: String) => client.sadd(ngrammListPrefix + ngram, term))
      }
    }
  }

  def get(ngram: String): Set[String] = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.smembers(ngrammListPrefix + ngram)
          .getOrElse(Set())
          .map(word => word.get)
      }
    }
  }

  def intersect(ngram1: String, ngrams: String*): Set[String] = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.sinter(ngrammListPrefix + ngram1, ngrams.map(ngram => ngrammListPrefix + ngram): _*)
          .getOrElse(Set())
          .map(word => word.get)
      }
    }
  }

  def erase(): Unit = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.keys(ngrammListPrefix + "*")
          .getOrElse(Set())
          .map({ key => key.getOrElse("") })
          .foreach(key => client.del(key))
      }
    }
  }
}
