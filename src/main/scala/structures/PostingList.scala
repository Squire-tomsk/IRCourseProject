package structures

import DAO.imp.redis.PostingListDAO

/**
  * Created by abuca on 12.11.16.
  */
class PostingList {
  val postingListDAO = new PostingListDAO

  def add(terms: List[String],docID:Long): Unit = {
    postingListDAO.add(terms, docID)
  }

  def getTf(docID:Long): Map[String,Long] ={
    postingListDAO.get(docID)
  }

  def getLogTf(docID:Long): Map[String,Double] ={
    val sum = postingListDAO.get(docID).values.sum.toDouble
    postingListDAO.get(docID).mapValues[Double](tf => 1.0 + math.log10(tf))
  }

  def erase(): Unit ={
    postingListDAO.erase()
  }
}
