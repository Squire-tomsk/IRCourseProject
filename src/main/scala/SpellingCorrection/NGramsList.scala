package SpellingCorrection

import DAO.imp.redis.NGramsListDAO

/**
  * Created by Anastasiia on 15.11.2016.
  */
class NGramsList {
  val ngramsListDAO = new NGramsListDAO

  def add(term: String): Unit = {
    ngramsListDAO.addTerm(term)
  }

  def get(ngram: String): Set[String] ={
    ngramsListDAO.get(ngram)
  }

  def intersect(ngram1: String, ngrams: String*): Set[String] ={
    ngramsListDAO.intersect(ngram1, ngrams: _*)
  }

  def intersectSet (ngrams: Set[String]): Set[String] ={
    if(ngrams.isEmpty) return Set()
    var result = intersect(ngrams.head)
    val tempngrams = ngrams.-(ngrams.head)
    for(ngram <- tempngrams){
      val temp = intersect(ngram)
      result = result.intersect(temp)
    }
    result
  }

  def erase(): Unit ={
    ngramsListDAO.erase()
  }
}
