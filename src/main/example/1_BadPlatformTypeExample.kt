package example

import com.github.rawls238.scientist4j.*

fun main(args: Array<String>) {
    val e: Experiment<Int> = Experiment("foo");
    val controlFunction = { null }
    val candidateFunction = { 32 }
    val result: Int = e.run(controlFunction, candidateFunction); //KABOOM!
    println("result=${result}") //We don't even get here
}