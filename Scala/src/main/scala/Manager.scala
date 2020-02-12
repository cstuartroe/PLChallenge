import java.io._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


object Manager {
  val initial_article = "Scala_(programming_language)"
  val to_scrape = new StringQueue(initial_article)
  val located = new StringBST(initial_article)
  val scraped = new StringBST()
  val excluded = new StringBST()

  val num_to_locate: Int = 20000
  val max_threads: Int = 3

  val excluded_prefixes: List[String] = List("Book","Category","Draft","File","H","Help","MOS","MediaWiki","Module","P","Portal","Special","Talk","Template","User","Wikipedia")

  var percent_finished: Int = 0
  var start_time: Long = _
  var total_threads: Int = 1

  def scrape(): Unit = {
    //println(s"beginning, total threads $total_threads")

    val title: String = to_scrape.dequeue()
    val ab = new ArticleBot(title)

    ab.scrape()

    for (l <- ab.links) {
      if (excluded_prefixes.exists(prefix => l.startsWith(prefix + ":") || l.startsWith(prefix + "_talk:"))) {
        excluded.push(l)
      } else if (!located.has(l)) { // checking so that to_scrape doesn't have duplicate entries
        located.push(l)
        to_scrape.enqueue(l)
      }
    }

    check_percent()

    scraped.push(title)
    //println(s"Scraped $title, now starting new threads, total threads $total_threads")
    total_threads -= 1

    while (total_threads < max_threads && to_scrape.getSize > 0 && located.getSize < num_to_locate) {
      total_threads += 1
      val x: Long = System.currentTimeMillis()
      val thr = Future {
        //println(s"$title has thread starting now")
        scrape()
      }
      //Thread.sleep(500)
      val elapsed: Long = System.currentTimeMillis() - x
      //println(s"took $elapsed millis to start, total threads $total_threads")
    }

    //println(s"$title finished adding threads, total $total_threads")

    if (total_threads == 0 && located.getSize >= num_to_locate) {
      terminate()
    }
  }

  def check_percent(): Unit = {
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

  def main(args: Array[String]): Unit = {
    percent_finished = 0
    start_time = System.currentTimeMillis()

    scrape()

    Thread.sleep(60000)
  }

  def terminate(): Unit = {
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
