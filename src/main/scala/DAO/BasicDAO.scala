package DAO

import com.redis.RedisClientPool

/**
  * Created by abuca on 04.11.16.
  */
object BasicDAO {
  val clienPool = new RedisClientPool("localhost",6379)
}
