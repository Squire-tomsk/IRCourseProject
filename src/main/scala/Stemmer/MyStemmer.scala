package Stemmer

import org.tartarus.martin.Stemmer

trait Stemming {
  def stem (word: String) : String
}
class MyStemmer extends Stemming{
  override def stem(word: String): String = {
    val stemmer = new Stemmer()
    stemmer.add(word.toCharArray, word.length)
    stemmer.stem
    stemmer.toString
  }
}
