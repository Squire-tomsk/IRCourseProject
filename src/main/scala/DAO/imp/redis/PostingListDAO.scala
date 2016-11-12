package DAO.imp.redis

import DAO.BasicDAO

/**
  * Created by abuca on 12.11.16.
  */
class PostingListDAO {
  val postingListPrefix = "postinglist:docID:"

  def add(terms: List[String],docID:Long): Unit ={
    BasicDAO.redisConnectionPool.withClient {
      client => {
        terms.foreach(term => (term,client.hincrby(postingListPrefix+docID,term,1)))
      }
    }
  }

  def get(docID:Long): Map[String,Long] ={
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.hgetall1(postingListPrefix+docID)
          .getOrElse(Map())
          .mapValues(tf => tf.toLong)
      }
    }
  }

  def erase(): Unit ={
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.keys(postingListPrefix + "*")
          .getOrElse(List())
          .map({ key => key.getOrElse("") })
          .foreach(key => client.del(key))
      }
    }
  }
}
