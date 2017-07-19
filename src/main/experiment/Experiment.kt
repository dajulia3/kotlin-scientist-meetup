package experiment
import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.Slf4jReporter
import com.github.rawls238.scientist4j.*
import java.util.function.BiFunction
import javax.print.attribute.IntegerSyntax

class Experiment<T>private constructor(name: String, metricRegistry: MetricRegistry, comparator: BiFunction<T, T, Boolean>) {
    private val innerExperiment = ExperimentBuilder<T>().
            withComparator(comparator)
            .withName(name)
            .withRegistry(metricRegistry)
            .build()

    constructor(name: String) : this(name, MetricRegistry(), BiFunction<T, T, Boolean> { obj1, obj2 -> obj1 == obj2 })
    private constructor(builder : Builder<T>) : this(builder.name, builder.registry, builder.comparator )

    companion object {
        fun <T>setup(init: Builder<T>.() -> Unit) :Experiment<T> { return Builder(init).build() }
    }

    class Builder<T> private constructor() {

        constructor(init: Builder<T>.() -> Unit) : this() {
            init()
        }

        lateinit var name: String
        lateinit var comparator: BiFunction<T, T, Boolean>
        lateinit var registry: MetricRegistry

        fun build() = Experiment(this)
    }

    fun run(control: ()-> T, candidate: () -> T) : T {
        val run : T = innerExperiment.run(control, candidate)
        return run
    }

}
