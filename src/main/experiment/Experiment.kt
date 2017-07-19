package experiment
import com.github.rawls238.scientist4j.*

class Experiment<T>(name: String) {
    private val innerExperiment = com.github.rawls238.scientist4j.Experiment<T>(name)

    fun run(control: ()-> T, candidate: () -> T) : T {
        val run : T = innerExperiment.run(control, candidate)
        return run //Will prevent a platform type from leaking!
    }

}
