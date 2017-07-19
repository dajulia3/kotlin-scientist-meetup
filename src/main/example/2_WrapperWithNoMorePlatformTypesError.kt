//package example
//
//import experiment.Experiment //This changed!
//
///* Hooray! Our old code doesn't even compile! */
//fun main(args: Array<String>) {
//    val e: Experiment<Int> = Experiment("foo");
//    val controlFunction = { null }
//    val candidateFunction = { 32 }
//    val result : Int = e.run(controlFunction, candidateFunction) //No more runtime Kaboom!
//    println("result=${result}")
//}