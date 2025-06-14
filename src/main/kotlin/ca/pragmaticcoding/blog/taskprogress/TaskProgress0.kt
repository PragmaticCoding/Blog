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

class TaskProgress0App : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 300.0, 300.0)
        stage.title = "Task Progress Example 0"
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        val task = object : Task<Unit>() {
            init {
                updateMessage("Not Started")
            }

            override fun call() {
                updateProgress(0, 100)
                updateMessage("Initializing")
                Thread.sleep(Duration.ofMillis(3000))
                updateProgress(30, 100)
                updateMessage("Processing")
                for (x in 31..90) {
                    updateProgress(x.toLong(), 100)
                    Thread.sleep(52.toLong())
                }
                updateMessage("Verifying")
                Thread.sleep(2000.toLong())
                updateProgress(100, 100)
                updateMessage("Completed")
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

fun main() {
    Application.launch(TaskProgress0App::class.java)
}