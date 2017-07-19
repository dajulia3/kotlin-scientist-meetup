package example

import com.codahale.metrics.ConsoleReporter
import com.codahale.metrics.MetricRegistry
import experiment.Experiment
import rx.Observable
import java.util.concurrent.TimeUnit
import java.util.function.BiFunction


fun main(args: Array<String>) {

    val metricRegistry = MetricRegistry()
    val reporter = ConsoleReporter.forRegistry(metricRegistry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build();
    reporter.start(1, TimeUnit.SECONDS);

    //This will produce results which will be printed to console every 1/4 second
    //Results are printed via the MetricRegistry's ConsoleReporter.
    //
    //We should see some variability in metrics due to the random function chosen as
    //for the candidate.
    //Roughly 2/3rd of the time, it will not match the control.
    //So that we can follow along in the console, I print the result of both the control
    //and the candidate function.
    everyQuarterSecondRunInTheBackground { invocationCount ->
        val result = Experiment.setup<Int> {
            name = "willy wonka's new cola"
            comparator = BiFunction<Int, Int, Boolean> { x, y -> x == y }
            registry = metricRegistry
        }.run(
                { 1 },
                {
                    randomIntFrom1To3().apply { print("($invocationCount) candidate=$this") }
                })
        println(" | control=${result}")
    }


    //keep running until the user terminates the program.
    while (true) {
        Thread.sleep(10)
    }
}

fun randomIntFrom1To3() = (Math.random() * 3).toInt() + 1
fun everyQuarterSecondRunInTheBackground(action: (elapsedTime: Long) -> Unit): Unit {
    Observable
            .interval(250, TimeUnit.MILLISECONDS)
            .subscribe({ invocationCount -> action(invocationCount) })
}