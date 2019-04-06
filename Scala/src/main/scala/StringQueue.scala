class StringQueue() {
  private var front: StringQueueNode = _
  private var back: StringQueueNode = _
  private var size: Int = 0

  def this(s: String) {
    this
    this.enqueue(s)
  }

  def enqueue(s: String): Unit = {
    if (front == null) {
      front = new StringQueueNode(s)
      back = front
    } else {
      back.next = new StringQueueNode(s)
      back = back.next
    }
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