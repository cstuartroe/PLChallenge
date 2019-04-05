class StringQueue(s: String) {
  private var front = new StringQueueNode(s)
  private var back = front
  private var size: Int = 1

  def enqueue(s: String): Unit = {
    back.next = new StringQueueNode(s)
    back = back.next
    size += 1
  }

  def dequeue(): String = {
    val s: String = front.s
    front = front.next
    size -= 1
    s
  }

  def getSize: Int = size
}

class StringQueueNode(val s: String) {
  var next: StringQueueNode = _

  def setNext(sqn : StringQueueNode) : Unit = {
    next = sqn
  }
}