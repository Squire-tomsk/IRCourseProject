package DAO.imp.redis

import DAO.BasicDAO

/**
  * Created by abuca on 04.11.16.
  */
class WikipediaCrawlerDAO {
  val crawledTitlesKey = "crawler:wikipedia:crawled_title_set"
  val linksList = "crawler:wikipedia:links_list"

  def addToCrawledSet(title: String):Unit ={
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.sadd(crawledTitlesKey,title)
      }
    }
  }

  def isTitleCrawled(title: String):Boolean ={
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.sismember(crawledTitlesKey,title)
      }
    }
  }

  def addLink(link: String):Unit ={
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.rpush(linksList,link)
      }
    }
  }

  def getLinksList:List[String] ={
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.lrange[String](linksList,0,-1).
          getOrElse(List()).
          map(link => link.get)
      }
    }
  }

  def getLinksListLength:Long ={
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.llen(linksList).getOrElse(0)
      }
    }
  }

  def erace() = {
    BasicDAO.redisConnectionPool.withClient {
      client => {
        client.del(crawledTitlesKey)
        client.del(linksList)
      }
    }
  }
}
