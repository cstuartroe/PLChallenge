class ArticleBot(val name: String) {
  private val BASE_URL = "https://en.wikipedia.org/wiki/"
  private val url = BASE_URL + name
  var links: List[String] = Nil

  def getContent: String = scala.io.Source.fromURL(url).mkString

  def scrape() : Unit = {
    val content = getContent
    val link_re = "href=\"/wiki/([A-Za-z0-9_\\(\\):]+)".r

    for (m <- link_re.findAllIn(content).matchData) links = m.group(1) :: links
  }
}
