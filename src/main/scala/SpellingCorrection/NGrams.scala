package SpellingCorrection

/**
  * Created by Anastasiia on 14.11.2016.
  */
class NGrams {
  def make(word: String): Set[String] = {
    word.sliding(3).toSet
  }
}
