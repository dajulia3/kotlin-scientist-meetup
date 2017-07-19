package example

import experiment.Experiment //We import OUR experiment now. Not scientist4j's

//Using our new wrapper library
fun main(args: Array<String>) {
    val e: Experiment<Int> = Experiment("foo");
    val controlFunction = { 32 }
    val candidateFunction = { 32 }
    val result: Int = e.run(controlFunction, candidateFunction); //No more runtime Kaboom!
    println("result=${result}")
}