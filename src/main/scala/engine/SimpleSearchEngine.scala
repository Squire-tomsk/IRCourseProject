package engine

import DAO.imp.postgres.PostgresDocumentDAO
import structures.{Dictionary, PostingList}
import utils.{DocToVecConverter, TermExtractor}

/**
  * Created by abuca on 12.11.16.
  */
class SimpleSearchEngine {
  val documentDAO = new PostgresDocumentDAO
  val postingLists = new PostingList
  val converter = new DocToVecConverter
  val extractor = new TermExtractor
  val dictionary = new Dictionary

  def search(query: String): List[(Long,Double)] = {
    val terms = extractor.extract(query)

    val docPool = terms.
      map(term => dictionary.getDocIDSet(term)). //here
      reduce((set1,set2) => set1.union(set2)).
      toList

    val queryVector = converter.
      convert(query).
      map(tfEntety => (tfEntety._1,dictionary.getIdf(tfEntety._1)*tfEntety._2))

    val scores = docPool.
      map(docId => (docId,postingLists.getLogTf(docId))). //here
      map(tfMap => (tfMap._1,tfMap._2.filterKeys(term => queryVector.keySet.contains(term)))).
      map(tfMap => (tfMap._1,tfMap._2.map(tfEntety => (tfEntety._1,dictionary.getIdf(tfEntety._1)*tfEntety._2)))).
      map(tfidfMap => (tfidfMap._1,tfidfMap._2.map(tfidfEntety => queryVector.get(tfidfEntety._1).get*tfidfEntety._2).sum))

    scores.sortBy(entety => 1/entety._2)
  }
}
