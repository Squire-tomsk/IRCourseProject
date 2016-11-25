package engine

import DAO.imp.postgres.PostgresDocumentDAO
import DAO.traits.DocumentDAO
import SpellingCorrection.SpellingCorrector
import structures.{Dictionary, PostingList}
import utils.{DocToVecConverter, TermExtractor}
import scala.collection.JavaConversions._

/**
  * Created by abuca on 12.11.16.
  */
class SimpleSearchEngine {
  val documentDAO = DocumentDAO.getDAO
  val postingLists = new PostingList
  val converter = new DocToVecConverter
  val extractor = new TermExtractor
  val dictionary = new Dictionary
  val corrector = new SpellingCorrector

  def search(query: String): List[(Long,Double)] = {
    val terms = extractor.extract(query)
    val stopWords = dictionary.getStopWords()

    val docPool = terms.
      filter(term => !stopWords.contains(term)).
      map(term => dictionary.getDocIDSet(term)).
      reduce((set1,set2) => set1.union(set2)).
      toList

    val queryAddition = corrector.correct(query)

    val queryVector = converter.
      convert(query+" "+queryAddition).
      filter(tfEntety => !stopWords.contains(tfEntety._1)).
      map(tfEntety => (tfEntety._1,dictionary.getIdf(tfEntety._1)*tfEntety._2))

    /*val queryVector = converter.
      convert(query).
      filter(tfEntety => !stopWords.contains(tfEntety._1)).
      map(tfEntety => (tfEntety._1,dictionary.getIdf(tfEntety._1)*tfEntety._2))*/

    val scores = docPool.
      par.
      map(docId => (docId,postingLists.getLogTf(docId))).
      map(tfMap => (tfMap._1,tfMap._2.filterKeys(term => queryVector.keySet.contains(term)))).
      map(tfMap => (tfMap._1,tfMap._2.map(tfEntety => (tfEntety._1,dictionary.getIdf(tfEntety._1)*tfEntety._2)))).
      map(tfidfMap => (tfidfMap._1,tfidfMap._2.map(tfidfEntety => queryVector.get(tfidfEntety._1).get*tfidfEntety._2).sum)).
      seq.
      toList

    scores.sortBy(entety => 1/entety._2).filterNot(entety => entety._2.isInfinity).take(100)
  }

  def getDocuments(query: String): java.util.List[String] = {
    search(query).
      take(10).
      map(tuple => tuple._1).
      map(id => documentDAO.getDocument(id))
  }

  def getDocumentsOrLinks(query: String): java.util.List[String] = {
    search(query).
      take(10).
      map(tuple => tuple._1).
      map(id => documentDAO.getDocumentOrLink(id))
  }
}
