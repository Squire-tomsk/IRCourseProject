package Stemmer

import org.scalatest.{FlatSpec, Matchers}

class StemmerSpec extends FlatSpec with Matchers{
  "Stemmer" should "do Porter stemmer and return" in {
    val stemmer = new MyStemmer
    stemmer.stem("wastes") should be ("wast")
    stemmer.stem("wasting") should be ("wast")
    stemmer.stem("pony") should be ("poni")
    stemmer.stem("numerical") should be ("numer")
    stemmer.stem("sacrificing") should be ("sacrif")
    stemmer.stem("without") should be ("without")
  }
}
