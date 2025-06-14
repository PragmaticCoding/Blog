package ca.pragmaticcoding.blog.taskprogress

import javafx.application.Application
import javafx.concurrent.Task
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.time.Duration

class TaskProgress2App : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 300.0, 300.0)
        stage.title = "Task Progress Example 2"
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        val task = object : Task<Unit>() {
            init {
                updateMessage("Not Started")
            }

            override fun call() {
                handleProcessing({ done, total -> updateProgress(done, total) }, { updateMessage(it) })
            }
        }
        center = VBox(10.0).apply {
            children += Label().apply {
                textProperty().bind(task.messageProperty())
            }
            children += ProgressBar().apply {
                progressProperty().bind(task.progressProperty())
                minWidth = 200.0
            }
            alignment = Pos.CENTER
        }
        bottom = HBox(Button("Run Task").apply {
            setOnAction { Thread(task).start() }
        }).apply {
            alignment = Pos.CENTER
            padding = Insets(20.0)
        }
    }
}

fun handleProcessing(progressUpdater: (Long, Long) -> Unit, messageUpdater: (String) -> Unit) {
    progressUpdater(0, 100)
    messageUpdater("Initializing")
    Thread.sleep(Duration.ofMillis(3000))
    progressUpdater(30, 100)
    messageUpdater("Processing")
    for (x in 31..90) {
        progressUpdater(x.toLong(), 100)
        Thread.sleep(52.toLong())
    }
    messageUpdater("Verifying")
    Thread.sleep(2000.toLong())
    progressUpdater(100, 100)
    messageUpdater("Completed")
}

fun main() {
    Application.launch(TaskProgress2App::class.java)
}