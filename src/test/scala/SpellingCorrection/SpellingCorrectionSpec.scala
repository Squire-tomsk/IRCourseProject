package SpellingCorrection

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Anastasiia on 15.11.2016.
  */
class SpellingCorrectionSpec extends FlatSpec with Matchers{
  "Spelling Corrector" should "return top 10 (or less) results of correct word form" in {
    val corrector = new SpellingCorrector
    //corrector.correctTerm("adres")
    //corrector.damerau_levenshtein("time", "tmie") should be (1)
    corrector.levenshtein("time", "tmie") should be (2)
    //corrector.correctTerm("tiem") should be ("time")
    corrector.correctTerm("corrcetion") should be ("correction")
    corrector.correct("corrcetion phrase") should be ("correction phrase ")
  }
}
