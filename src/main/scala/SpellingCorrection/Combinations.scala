package SpellingCorrection

import java.util.ArrayList

/**
  * Created by Anastasiia on 16.11.2016.
  */
class Combinations {
  def factorial(number: Int) : Int = {
    def factorialWithAccumulator(accumulator: Int, number: Int) : Int = {
      if (number == 1 || number == 0)
        return accumulator
      else
        factorialWithAccumulator(accumulator * number, number - 1)
    }
    factorialWithAccumulator(1, number)
  }

  def C(n: Int, k: Int): Int = factorial(n) / (factorial(k) * factorial(n - k))

  def combination(index: Int, k: Int, a: Array[Int]): ArrayList[Int] = {
    val res = new ArrayList[Int]
    res.add(0)
    val n = a.length
    var s = 0
    for (t <- 1 to k) {
      var j = res.get(t - 1) + 1
      while ((j < (n - k + t)) && (s + C(n - j, k - t) <= index)) {
        s += C(n - j, k - t)
        j += 1
      }
      res.add(j)
    }
    res.remove(0)
    res
  }
}
