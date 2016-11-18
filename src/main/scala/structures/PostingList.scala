package structures

import DAO.imp.redis.PostingListDAO
import utils.TermExtractor

/**
  * Created by abuca on 12.11.16.
  */
class PostingList {
  val postingListDAO = new PostingListDAO
  val termExtractor = new TermExtractor


  def add(doc: String, docID:Long): Unit = {
    postingListDAO.add(termExtractor.extract(doc), docID)
    postingListDAO.refreshLogTf(docID)
  }

  private def add(terms: List[String],docID:Long): Unit = {
    postingListDAO.add(terms, docID)
  }

  def getTf(docID:Long): Map[String,Long] ={
    postingListDAO.getTf(docID)
  }

  def getLogTf(docID:Long): Map[String,Double] ={
    postingListDAO.getlogTf(docID)
  }

  def getLogTfCalculatedInRuntime(docID:Long): Map[String,Double] ={
    val sum = postingListDAO.getTf(docID).values.sum.toDouble
    postingListDAO.getTf(docID).mapValues[Double](tf => 1.0 + math.log10(tf))
  }

  def erase(): Unit ={
    postingListDAO.erase()
  }
}
