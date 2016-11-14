package SpellingCorrection

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Anastasiia on 15.11.2016.
  */
class BiGramsSpec extends FlatSpec with Matchers{
  "The BiGrams" should "return bigrams of one term" in {
    val bigramMaker = new BiGrams
    bigramMaker.make("good") should be (Set("go", "oo", "od"))
    bigramMaker.make("") should be (Set())
  }
}
