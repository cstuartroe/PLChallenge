object Manager {
  def main(args: Array[String]): Unit = {
    val initial_article = "Scala_(programming_language)"
    val sq = new StringQueue(initial_article)
    val sbst = new StringBST(initial_article)
    var ab: ArticleBot = null

    while (sbst.getSize < 100) {
      ab = new ArticleBot(sq.dequeue())
      ab.scrape()
      for (l <- ab.links) {
        sq.enqueue(l)
        sbst.push(l)
      }
    }

    sbst.printout()

    println("Successfully located " + sbst.getSize + " articles.")
  }
}
