package DAO.imp.redis

import DAO.BasicDAO

/**
  * Created by abuca on 12.11.16.
  */
class PostingListDAO {
  val postingListPrefix = "postinglist:docID:"

  def add(terms: List[String], docID: Long): Unit = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        terms.foreach(term => client.hincrby(postingListPrefix + docID + "tf", term, 1))
      }
    }
  }

  def refreshLogTf(docID: Long): Unit = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        val sum = getTf(docID).values.sum.toDouble
        getTf(docID).foreach(
          entety => client.hset(postingListPrefix + docID + "logtf", entety._1, 1.0 + math.log10(entety._2))
        )
      }
    }
  }

  def getTf(docID: Long): Map[String, Long] = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.hgetall1(postingListPrefix + docID + "tf")
          .getOrElse(Map())
          .mapValues(tf => tf.toLong)
      }
    }
  }

  def getlogTf(docID: Long): Map[String, Double] = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.hgetall1(postingListPrefix + docID + "logtf")
          .getOrElse(Map())
          .mapValues(tf => tf.toDouble)
      }
    }
  }

  def erase(): Unit = {
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
