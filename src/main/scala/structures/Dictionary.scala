package structures
import DAO.imp.postgres.PostgresDocumentDAO
import DAO.imp.redis.DictionaryDAO
import DAO.traits.DocumentDAO

/**
  * Created by abuca on 10.11.16.
  */
class Dictionary {
  val documentDAO: DocumentDAO = new PostgresDocumentDAO
  val dictionaryDAO: DictionaryDAO = new DictionaryDAO

  def add(word: String, docId: Long): Unit = {
    dictionaryDAO.add(word,docId)
  }

  def getIdf(word: String): Double = {
    Math.log10(
        documentDAO.getStoredDocumentCount.toDouble / dictionaryDAO.getFreq(word))
  }

  def getDocIDSet(word: String): Set[Long] = {
    dictionaryDAO.getDocIDSet(word)
  }

  def erace(): Unit ={
    dictionaryDAO.erase()
  }
}
