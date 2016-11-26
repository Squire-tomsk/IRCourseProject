package utils

/**
  * Created by abuca on 12.11.16.
  */
class DocToVecConverter {
  val extractor = new TermExtractor

  def convert(doc: String): Map[String, Double] = {
    extractor.extract(doc).
      map(term => (term -> 1)).
      groupBy[String](_._1).
      mapValues(a => a.length.toDouble).
      mapValues(tf => 1.0 + math.log10(tf))
  }
}
