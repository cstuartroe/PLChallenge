class StringBST {
  private var root: StringBSTNode = _
  private var size: Int = 0

  def this(s: String) {
    this
    this.push(s)
  }

  def push(s: String): Unit = {
    if (root == null) {
      root = new StringBSTNode(s)
      size += 1
    }
    else {
      if (root.push(s)) size += 1
    }
  }

  def has(s: String): Boolean = {
    if (root == null) false
    else root.has(s)
  }

  def getSize: Int = size

  def printout(): Unit = root.printout()
}

class StringBSTNode (val s: String) {
  private var left: StringBSTNode = _
  private var right: StringBSTNode = _

  // returns true if string was actually added, false if it was already present
  def push(other_s: String): Boolean = {
    if (other_s < s) {
      if (left == null) {
        left = new StringBSTNode(other_s)
        true
      } else {
        return left.push(other_s)
      }
    } else if (other_s > s) {
      if (right == null) {
        right = new StringBSTNode(other_s)
        true
      } else {
        return right.push(other_s)
      }
      true
    } else {
      false
    }
  }

  def has(other_s: String): Boolean = {
    if (other_s == s) true
    else
    if (other_s < s) {
      if (left == null) false
      else left.has(other_s)
    }
    else
    {
      if (right == null) false
      else right.has(other_s)
    }
  }

  def printout(): Unit = {
    if (left != null) left.printout()
    println(s)
    if (right != null) right.printout()
  }
}