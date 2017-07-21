package experiment
import com.codahale.metrics.MetricRegistry
import com.github.rawls238.scientist4j.ExperimentBuilder
import java.util.function.BiFunction

/*
The use of the typesafe builder pattern here is a bit contrived.
In fact, you get safer code by just using the constructor with named parameters.
Everything is guaranteed to be initialized!
However, I felt that it would be good to show how you could progress towards the builder pattern.
This example is not a great real-life use case!

A much better use case is building a structure with many optional or repeated items,
or building a deeply nested structure.

eg. HTML or another tree-like structure.

For example:
node {
    label = "root"
    node {
        label = "leaf1
    }

    node {
        label = "1st generation"
        node {
            label = "leaf2"
        }
    }

    specialNode{
        label = "Awesome leaf node"
        amISpecial = true
    }
}
 */
class Experiment<T> private constructor(name: String, metricRegistry: MetricRegistry, comparator: BiFunction<T, T, Boolean>) {
    private val innerExperiment = ExperimentBuilder<T>().
            withComparator(comparator)
            .withName(name)
            .withRegistry(metricRegistry)
            .build()

    constructor(name: String) : this(name, MetricRegistry(), BiFunction<T, T, Boolean> { obj1, obj2 -> obj1 == obj2 })
    private constructor(builder: Builder<T>) : this(builder.name, builder.registry, builder.comparator)

    companion object {
        //We pass a function literal with receiver as the only way of publicly constructing an Experiment.
        //We immediately invoke the builder's constructor with the passed in function, and build it.
        //Thus, we force people to use this setup method to construct an Experiment.
        //
        // Side note/left as an exercise to the reader:
        // You can approximate Swift's Failable Initializers language feature  using this pattern.
        fun <T> setup(init: Builder<T>.() -> Unit): Experiment<T> {
            return Builder(init).build()
        }
    }

    //Mark the constructor internal because only the Experiment.setup function should call it!
    //We also pass along an implicit receiver so that we can actually execute the
    //code that will set properties on the builder!
    class Builder<T> internal constructor(init: Builder<T>.() -> Unit) {
        init {
            init()
        }

        //all the properties are lateinit because they will be set by the
        //init function from the constructor.
        //As someone pointed out at the meetup, I _could_ have made these optional.
        //Then, I could have provided default values for anything that wasn't set.
        lateinit var name: String
        lateinit var comparator: BiFunction<T, T, Boolean>
        lateinit var registry: MetricRegistry

        //It's not strictly necessary to expose these as methods since the properties are public
        fun name(init: Builder<T>.() -> String) { name = init() }
        fun registry(init: Builder<T>.() -> MetricRegistry) { registry = init() }
        fun comparator(init: Builder<T>.() -> BiFunction<T, T, Boolean>) { comparator = init() }

        //Again, let's keep this internal to force people through our Experiment.setup function.
        internal fun build() = Experiment(this)
    }

    fun run(control: () -> T, candidate: () -> T): T {
        val run: T = innerExperiment.run(control, candidate)
        return run
    }

}
