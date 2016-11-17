package SpellingCorrection

import DAO.BasicDAO
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Anastasiia on 15.11.2016.
  */
class NGramsListSpec extends FlatSpec with Matchers{
  "The ngramsList" should "store ngram associated with set of terms" in {
    BasicDAO.init()
    val ngramlist = new NGramsList
    ngramlist.add("myword")
    ngramlist.add("mywtea")
    ngramlist.get("tea") should be (Set("mywtea"))
    ngramlist.get("myw") should be (Set("mywtea", "myword"))
    ngramlist.intersect("myw", "wte") should be (Set("mywtea"))
    ngramlist.intersectSet(Set("myw", "wte")) should be (Set("mywtea"))
    ngramlist.intersect("tea") should be (Set("mywtea"))
    ngramlist.erase()
    ngramlist.add("mooood")
    ngramlist.get("ooo") should be(Set("mooood"))
    /*ngramlist.get("tea") should be (Set())
    ngramlist.get("te") should be (Set("mytea"))
    ngramlist.get("ea") should be (Set("mytea"))
    ngramlist.get("my") should be (Set("mytea", "myword"))
    ngramlist.intersect("my", "te", "ea") should be (Set("mytea"))
    ngramlist.intersect("my", "te") should be (Set("mytea"))
    ngramlist.erase()
    ngramlist.get("my") should be (Set())
    ngramlist.add("moood")
    ngramlist.get("oo") should be(Set("moood"))*/
  }
}
