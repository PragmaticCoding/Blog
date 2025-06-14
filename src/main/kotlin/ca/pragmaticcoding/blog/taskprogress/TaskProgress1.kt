package ca.pragmaticcoding.blog.taskprogress

import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.concurrent.Task
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Region
import javafx.stage.Stage
import javafx.util.Builder
import java.util.concurrent.atomic.AtomicReference

enum class ProcessingStage { STAGE1, STAGE2, STAGE3, STAGE4, DONE }

class Model {
    val currentStage = SimpleObjectProperty(ProcessingStage.STAGE1)
}

class Controller {
    private val model = Model()
    private val interactor = Interactor(model)
    private val viewBuilder = ViewBuilder(model) { launchTask() }

    fun getView() = viewBuilder.build()

    private fun launchTask() {
        val task = object : Task<Unit>() {
            val stageUpdate = AtomicReference<ProcessingStage?>()
            val stage = SimpleObjectProperty(ProcessingStage.STAGE1)
            override fun call() {
                interactor.doProcessing { updateStage(it) }
            }

            fun updateStage(newStage: ProcessingStage) {
                if (Platform.isFxApplicationThread()) {
                    stage.set(newStage)
                } else if (stageUpdate.getAndSet(newStage) == null) {
                    Platform.runLater {
                        stage.set(stageUpdate.getAndSet(null))
                    }
                }
            }
        }
        task.setOnSucceeded { model.currentStage.unbind() }
        model.currentStage.bind(task.stage)
        Thread(task).start()
    }
}

class ViewBuilder(private val model: Model, private val taskRunner: () -> Unit) : Builder<Region> {
    override fun build(): Region = BorderPane().apply {
        center = Label().apply {
            textProperty().bind(model.currentStage.asString())
        }
        bottom = Button("Click Me").apply {
            setOnAction { taskRunner.invoke() }
        }
    }
}

class Interactor(private val model: Model) {

    fun doProcessing(updater: (ProcessingStage) -> Unit) {
        doStep1()
        updater(ProcessingStage.STAGE2)
        doStep2()
        updater(ProcessingStage.STAGE3)
        doStep3()
        updater(ProcessingStage.STAGE4)
        doStep4()
        updater(ProcessingStage.DONE)
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

class TaskProgress1App : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(Controller().getView(), 300.0, 300.0)
        stage.show()
    }
}

fun main() {
    Application.launch(TaskProgress1App::class.java)
}