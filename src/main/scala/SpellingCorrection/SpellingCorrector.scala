package SpellingCorrection

import utils.TermExtractor


/**
  * Created by Anastasiia on 15.11.2016.
  */
class SpellingCorrector {
  val ngramsCollection = new NGramsCollection
  val termExtractor = new TermExtractor

  def correct(query: String): String = {
    val terms = termExtractor.extract(query)
    var result = ""
    for (term <- terms) {
      val variance = correctTerm(term).foreach(v => result += (v+" "))
    }
    result.dropRight(1)
  }

  def correctTerm(term: String): Set[String] = {
    val ngrams = new NGrams
    val termngrams = ngrams.make(term)
    val bigramNumber = termngrams.size
    val bigramIndexes = new Array[Int](bigramNumber)
    val combinatoric = new Combinations
    var variants = collection.mutable.Set[String]()

    for (index <- bigramIndexes.indices) {
      bigramIndexes(index) = index
    }

    for (i <- 0 until combinatoric.C(termngrams.size, bigramNumber / 2)) {
      val bigramComboCode = combinatoric.combination(i, bigramNumber / 2, bigramIndexes)
      var termngramsCombo = collection.mutable.Set[String]()
      for (i <- 0 until bigramComboCode.size()) {
        val index = bigramComboCode.get(i) - 1
        if (index < termngrams.size) {
          termngramsCombo += termngrams.toArray.apply(index)
        }
      }
      variants ++= ngramsCollection.ngramslist.intersectSet(termngramsCombo.toSet)
    }
    var max = 0.0
    var rating = collection.mutable.Map[String, Double]()

    for (variant <- variants) {
      val temp = jaccard(termngrams, ngrams.make(variant))
      if (temp > max) max = temp
      rating += (variant -> temp)
    }
    if (rating.size > 10) {
      rating.toSeq.sortBy(_._2).dropRight(10).reverse.toMap
    }
    for (variant <- rating) {
      rating(variant._1) = levenshtein(term, variant._1)
    }

    rating.filter(f => f._2 == rating.valuesIterator.min).keySet.toSet
  }

  def jaccard (ngrams1: Set[String], ngrams2: Set[String]): Double = {
    ngrams1.intersect(ngrams2).size.toDouble /
      ngrams1.union(ngrams2).size.toDouble
  }

  def levenshtein(str1: String, str2: String): Int = {
    val lenStr1 = str1.length
    val lenStr2 = str2.length

    val d: Array[Array[Int]] = Array.ofDim(lenStr1 + 1, lenStr2 + 1)

    for (i <- 0 to lenStr1) d(i)(0) = i
    for (j <- 0 to lenStr2) d(0)(j) = j

    for (i <- 1 to lenStr1; j <- 1 to lenStr2) {
      val cost = if (str1(i - 1) == str2(j-1)) 0 else 1

      d(i)(j) = min(
        d(i-1)(j  ) + 1,     // deletion
        d(i  )(j-1) + 1,     // insertion
        d(i-1)(j-1) + cost   // substitution
      )
    }

    d(lenStr1)(lenStr2)
  }

  def min(nums: Int*): Int = nums.min
  /*def damerau_levenshtein (word1: String, word2: String): Int = {
    val da = Array.fill(26)(0)
    val distances = Array.ofDim[Int](word1.length + 2, word2.length + 2)
    val maxdist = word1.length + word2.length
    distances(0)(0) = maxdist

    for (i <- 1 to word1.length + 1) {
      distances(i)(0) = maxdist
      distances(i)(1) = i
    }
    for (j <- 1 to word2.length + 1) {
      distances(0)(j) = maxdist
      distances(1)(j) = j
    }
    for (i <- 2 to word1.length + 1) {
      var db = 1
      for (j <- 2 to word2.length + 1) {
        val k = da(word2(j - 2) - 'a')
        val l = db
        var cost = 0
        if (word1(i - 2) == word2(j - 2)) {
          db = j
        } else {
          cost = 1
        }
        distances(i)(j) = Math.min(Math.min(Math.min(distances(i-1)(j-1) + cost, distances(i)(j-1) + 1),
          distances(i-1)(j) + 1),
          distances(k)(l-1) + (i-k) + 1 + (j-l-1))
      }
      da(word1(i - 2) - 'a') = i - 1
    }
    distances(word1.length + 1)(word2.length + 1)
  }*/
}
