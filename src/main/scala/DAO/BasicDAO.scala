package DAO

import com.redis.RedisClientPool
import scalikejdbc.config._

/**
  * Created by abuca on 04.11.16.
  */
object BasicDAO {
  val redisConnectionPool = new RedisClientPool("localhost",6379)

  def init(): Unit ={
    DBs.setupAll()
  }
}
