package SpellingCorrection

import java.io.{FileNotFoundException, IOException}

import scala.io.Source


/**
  * Created by Anastasiia on 15.11.2016.
  */
class NGramsCollection {
  val ngramslist = new NGramsList

  def create(path: String): Unit = {
    //val path = "https://raw.githubusercontent.com/dwyl/english-words/master/words.txt"
    try {
      for (term <- Source.fromFile(path).getLines()) {
        ngramslist.add(term)
      }
    } catch {
      case ex: FileNotFoundException => println("Couldn't find that file.")
      case ex: IOException => println("Had an IOException trying to read that file")
    }
  }

  def erase(): Unit = {
    ngramslist.erase()
  }
}
