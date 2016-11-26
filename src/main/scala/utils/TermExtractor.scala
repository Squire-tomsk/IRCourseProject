package utils

import edu.stanford.nlp.simple.Document
import org.jsoup.Jsoup

import scala.collection.JavaConversions._

class TermExtractor {
  def extract(doc: String): List[String] = {
    val text = Jsoup.parse(doc).body().text()
    val document = new Document(text)
    document.
      sentences().
      toList.
      map(sentence => sentence.lemmas()).
      flatten.
      distinct.
      map(term => term.replaceAll("[^a-zA-Z]", " ").toLowerCase).
      map(term => term.split(" ")).
      flatten.
      filter(term => !term.isEmpty)
  }
}
