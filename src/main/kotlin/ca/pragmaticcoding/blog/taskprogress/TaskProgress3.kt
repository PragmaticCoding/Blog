package ca.pragmaticcoding.blog.taskprogress

import ca.pragmaticcoding.examples.pseudo.PseudoClassSupplier
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.concurrent.Task
import javafx.css.PseudoClass
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Region
import javafx.stage.Stage
import javafx.util.Builder
import java.util.concurrent.atomic.AtomicReference


enum class ProgressPseudoClass : PseudoClassSupplier {
    STAGE1, STAGE2, STAGE3, STAGE4, DONE;

    companion object PseudoClasses {
        val stage1PseudoClass: PseudoClass = PseudoClass.getPseudoClass("stage1")
        val stage2PseudoClass: PseudoClass = PseudoClass.getPseudoClass("stage2")
        val stage3PseudoClass: PseudoClass = PseudoClass.getPseudoClass("stage3")
        val stage4PseudoClass: PseudoClass = PseudoClass.getPseudoClass("stage4")
        val donePseudoClass: PseudoClass = PseudoClass.getPseudoClass("done")
    }

    override fun getPseudoClass() = when (this) {
        STAGE1 -> stage1PseudoClass
        STAGE2 -> stage2PseudoClass
        STAGE3 -> stage3PseudoClass
        STAGE4 -> stage4PseudoClass
        DONE -> donePseudoClass
    }
}

class Model3 {
    val currentStage = SimpleObjectProperty(ProgressPseudoClass.DONE)
}

class Controller3 {
    private val model = Model3()
    private val interactor = Interactor3(model)
    private val viewBuilder = ViewBuilder3(model) { launchTask() }

    fun getView() = viewBuilder.build()

    private fun launchTask() {

        val task = object : StagedTask<Unit, ProgressPseudoClass>() {
            override fun call() {
                interactor.doProcessing { updateStage(it) }
            }
        }

        task.setOnSucceeded { model.currentStage.unbind() }
        model.currentStage.bind(task.stage)
        Thread(task).start()
    }
}

class ViewBuilder3(private val model: Model3, private val taskRunner: () -> Unit) : Builder<Region> {
    override fun build(): Region = BorderPane().apply {
        center = Label().apply {
            textProperty().bind(model.currentStage.asString())
            styleClass += "progress-label"
            model.currentStage.subscribe { oldVal, newVal ->
                oldVal?.let { pseudoClassStateChanged(it.getPseudoClass(), false) }
                newVal?.let { pseudoClassStateChanged(it.getPseudoClass(), true) }
            }
        }
        bottom = Button("Click Me").apply {
            setOnAction { taskRunner.invoke() }
        }
    }
}

class Interactor3(private val model: Model3) {

    fun doProcessing(updater: (ProgressPseudoClass) -> Unit) {
        doStep1()
        updater(ProgressPseudoClass.STAGE2)
        doStep2()
        updater(ProgressPseudoClass.STAGE3)
        doStep3()
        updater(ProgressPseudoClass.STAGE4)
        doStep4()
        updater(ProgressPseudoClass.DONE)
    }

    private fun doStep1() {
        Thread.sleep(5000)
    }

    private fun doStep2() {
        Thread.sleep(5000)
    }

    private fun doStep3() {
        Thread.sleep(5000)
    }

    private fun doStep4() {
        Thread.sleep(5000)
    }
}

class TaskProgress3App : Application() {
    override fun start(stage: Stage) {
        println("Running Example3")
        stage.scene = Scene(Controller3().getView(), 300.0, 300.0).apply {
            TaskProgress3App::class.java.getResource("tasks.css")?.toString()?.let {
                println("adding stylesheet")
                stylesheets += it
            }
        }
        stage.show()
    }
}

fun main() {
    Application.launch(TaskProgress3App::class.java)
}

abstract class StagedTask<T, S>(initialStage: S? = null) : Task<T>() {
    private val stageUpdate = AtomicReference<S?>()
    val stage = SimpleObjectProperty<S?>(initialStage)

    fun updateStage(newStage: S) {
        if (Platform.isFxApplicationThread()) {
            stage.set(newStage)
        } else if (stageUpdate.getAndSet(newStage) == null) {
            Platform.runLater {
                stage.set(stageUpdate.getAndSet(null))
            }
        }
    }
}