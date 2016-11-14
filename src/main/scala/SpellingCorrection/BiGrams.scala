package SpellingCorrection

/**
  * Created by Anastasiia on 14.11.2016.
  */
class BiGrams {
  def make(word: String): Set[String] = {
    word.sliding(2).toSet
  }
}
