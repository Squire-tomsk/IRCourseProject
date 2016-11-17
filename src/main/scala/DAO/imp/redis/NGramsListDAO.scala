package DAO.imp.redis

import DAO.BasicDAO
import SpellingCorrection.NGrams

/**
  * Created by Anastasiia on 14.11.2016.
  */
class NGramsListDAO{
  val postingListPrefix = "ngramlist:ngram:"

  def addTerm(term: String): Unit = {
    val ngramMaker = new NGrams
    val ngrams = ngramMaker.make(term)
    BasicDAO.redisConnectionPool.withClient {
      client => {
        ngrams.foreach((ngram: String) => client.sadd(postingListPrefix + ngram, term))
      }
    }
  }

  def get(ngram: String): Set[String] ={
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.smembers(postingListPrefix + ngram)
          .getOrElse(Set())
          .map(word => word.get)
      }
    }
  }

  def intersect(ngram1: String, ngrams: String* ): Set[String] ={
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.sinter(postingListPrefix + ngram1, ngrams.map(ngram => postingListPrefix + ngram): _*)
          .getOrElse(Set())
          .map(word => word.get)
      }
    }
  }

  /*def intersect(ngrams: Set[String]): Set[String] ={

    BasicDAO.redisConnectionPool.withClient {
      client => {
       // client.sinter(ngrams.map(ngram => postingListPrefix + ngram))
        client.sinter(serializeSetStrings(ngrams))
          .getOrElse(Set())
          .map(word => word.get)
      }
    }
  }
  def serializeSetStrings(set: Set[String]) : String = {
    var resset = set.map(f =>  postingListPrefix + f)
    resset = resset.map(f => "$" + f.length + "\r\n" + f + "\r\n")
    var result = "*" + resset.size + "\r\n"
    for (item <- resset) {
      result += item
    }
    result
  }*/

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
