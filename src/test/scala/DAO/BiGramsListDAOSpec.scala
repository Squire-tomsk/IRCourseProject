package DAO

import DAO.imp.redis.BiGramsListDAO
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Anastasiia on 15.11.2016.
  */
class BiGramsListDAOSpec extends FlatSpec with Matchers{
  "The BiGramsListDAO" should "store bigram associated with set of terms" in {
    BasicDAO.init()
    val bigramlist = new BiGramsListDAO
    bigramlist.addTerm("myword")
    bigramlist.addTerm("mytea")
    bigramlist.get("tea") should be (Set())
    bigramlist.get("te") should be (Set("mytea"))
    bigramlist.get("my") should be (Set("mytea", "myword"))
    bigramlist.erase()
    bigramlist.get("my") should be (Set())
    bigramlist.addTerm("moood")
    bigramlist.get("oo") should be(Set("moood"))
  }
}
