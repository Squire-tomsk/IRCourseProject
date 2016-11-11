package utils

import edu.stanford.nlp.simple.Document
import scala.collection.JavaConversions._
import org.jsoup.Jsoup

class TermExtractor{
  def extract(doc: String): List[String] = {
    val document = new Document(Jsoup.parse(doc).body().text())
    document.
      sentences().
      toList.
      map(sentence => sentence.lemmas()).
      flatten.
      distinct.
      map(term => term.replaceAll("[^a-zA-Z]"," ").toLowerCase).
      map(term => term.split(" ")).
      flatten.
      filter(term => !term.isEmpty)
  }
}

/*
class TermExtractor{
  def extract(doc: String): List[String] = {
    val document = new Document(Jsoup.parse(doc).body().text())
    document.
      sentences().
      toList.
      map(sentence => sentence.lemmas()).
      flatten.
      distinct.
      map(term => term.replace("[^a-zA-Z]"," ")).
      map(term => term.split(" ")).
      flatten.
      filter(term => !term.isEmpty)
  }
}
 */