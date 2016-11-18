package SpellingCorrection

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Anastasiia on 15.11.2016.
  */
class SpellingCorrectionSpec extends FlatSpec with Matchers{
  "Spelling Corrector" should "return top 10 (or less) results of correct word form" in {
    val corrector = new SpellingCorrector
    //corrector.damerau_levenshtein("time", "tmie") should be (1)
    corrector.levenshtein("time", "tmie") should be (2)
    
    //corrector.correctTerm("corrcetion") should be (Set("correction", "corruption", "correption"))
    //corrector.correct("corrcetion phrase") should be ("correction corruption correption phrase")

    //for test.txt
    corrector.correct("tmeplate") should be ("template")
    corrector.correct("tmie") should be ("")
    corrector.correctTerm("tmie") should be (Set())

  }
}
