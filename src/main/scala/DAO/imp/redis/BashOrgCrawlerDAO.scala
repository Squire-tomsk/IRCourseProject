package DAO.imp.redis

import DAO.BasicDAO

/**
  * Created by abuca on 04.11.16.
  */
class BashOrgCrawlerDAO {
  val amountOfDocuments = 961099
  val notCrawledListKey = "document:not_crawled_number_list"
  val crawledListKey = "document:crawled_number_list"

  def getNotCraledDocumentNumbersList: List[Int] = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        if (!client.exists(notCrawledListKey)) {
          1 to amountOfDocuments map { number => client.rpush(notCrawledListKey, number) }
        }
        val crawledList = getCraledDocumentNumbersList
        val notCrawledList =
          client.lrange(notCrawledListKey, 0, -1).getOrElse(List())
            .map({ number => number.getOrElse("-1") })
            .map({ number => number.toInt })
            .filterNot(crawledList.toSet)
        notCrawledList
      }
    }
  }

  def getCraledDocumentNumbersList: List[Int] = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        if (!client.exists(crawledListKey)) {
          client.rpush(crawledListKey, -1)
        }
        client.lrange(crawledListKey, 0, -1).getOrElse(List())
            .map({ number => number.getOrElse("-1") })
            .map({ number => number.toInt })
      }
    }
  }

  def addCrawledDocumentNumber(crawledNumber: Int) = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.rpush(crawledListKey, crawledNumber)
      }
    }
  }

  def getCraledDocumentNumbersListLength: Long = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        if (!client.exists(crawledListKey)) {
          0
        }
        else {
          client.llen(crawledListKey).getOrElse(0)
        }
      }
    }
  }

  def erace() = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.del(notCrawledListKey)
        client.del(crawledListKey)
      }
    }
  }
}
