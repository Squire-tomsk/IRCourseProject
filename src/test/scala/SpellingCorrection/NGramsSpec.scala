package SpellingCorrection

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Anastasiia on 15.11.2016.
  */
class NGramsSpec extends FlatSpec with Matchers{
  "The ngrams" should "return ngrams of one term" in {
    val ngramMaker = new NGrams
    ngramMaker.make("good") should be (Set("goo", "ood"))
    ngramMaker.make("gooood") should be (Set("goo", "ooo", "ood"))
    ngramMaker.make("") should be (Set())
  }
}
