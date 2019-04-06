import java.io._

object Manager {
  def main(args: Array[String]): Unit = {
    val initial_article = "Scala_(programming_language)"
    val to_scrape = new StringQueue(initial_article)
    val located = new StringBST(initial_article)
    val scraped = new StringBST()
    val excluded = new StringBST()

    val num_to_locate: Int = 500000
    var percent_finished: Int = 0
    val start_time: Long = System.currentTimeMillis()

    val excluded_prefixes: List[String] = List("Book","Category","Draft","File","H","Help","MOS","MediaWiki","Module","P","Portal","Special","Talk","Template","User","Wikipedia")

    while (located.getSize < num_to_locate && to_scrape.getSize > 0) {
      val title: String = to_scrape.dequeue()

      scraped.push(title)
      val ab = new ArticleBot(title)

      try {
        ab.scrape()

        for (l <- ab.links) {
          if (excluded_prefixes.exists(prefix => l.startsWith(prefix+":") || l.startsWith(prefix+"_talk:"))) {
            excluded.push(l)
          } else if (!located.has(l)){ // checking so that to_scrape doesn't have duplicate entries
            located.push(l)
            to_scrape.enqueue(l)
          }
        }
      } catch {
        case fnfe: FileNotFoundException => {
          println("Could not find " + title)
        }
      }

      if (located.getSize >= num_to_locate * (percent_finished+5) / 100) {
        val elapsed_millis = System.currentTimeMillis() - start_time
        val elapsed_minutes = "%02d".format(elapsed_millis / 60000)
        val elapsed_seconds = "%02d".format((elapsed_millis % 60000) / 1000)

        percent_finished = located.getSize * 100 / num_to_locate
        println(
          s"""$percent_finished% finished (${located.getSize} located, ${excluded.getSize} excluded, ${scraped.getSize} scraped,
             |${to_scrape.getSize} queued) time elapsed $elapsed_minutes:$elapsed_seconds""".stripMargin.replace("\n"," "))
      }
    }

    println(s"Scraped ${scraped.getSize} articles, located a total of ${located.getSize}")
    println(s"Excluded ${excluded.getSize} articles")

    var file = new File("proper-nouns.txt")
    var bw = new BufferedWriter(new FileWriter(file))
    bw.write(located.printout())
    bw.close()

    file = new File("excluded.txt")
    bw = new BufferedWriter(new FileWriter(file))
    bw.write(excluded.printout())
    bw.close()
  }
}
