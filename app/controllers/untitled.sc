def fact(n: Int): Int = if(n<2) 1 else n*fact(n-1)

def sumSquares(n:Int):Int = if(n<2) 1 else n*n+sumSquares(n-2)

def countDown(n: Int): Unit = {
  if(n>0){
    println(n)
    countDown(n-1)
  }
}

def matcher(b: Int ) = {

  val expr = b - 10


  expr match {
    case 0 => 1
    case _ > 2 => "More"
    case _ < 0 => "Less"
  }
}

println(matcher(5))