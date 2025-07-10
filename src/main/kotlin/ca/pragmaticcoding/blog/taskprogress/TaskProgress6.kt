package ca.pragmaticcoding.blog.taskprogress

import ca.pragmaticcoding.widgetsfx.promptOf
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.concurrent.Task
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.time.Duration
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class TaskProgress6App : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 800.0, 500.0)
        stage.title = "Task Progress Example"
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        val task = object : Task<Int>() {

            private val dataList = mutableListOf<String>()
            val valueList: ListProperty<String> = SimpleListProperty(FXCollections.observableArrayList())
            val counter = SimpleIntegerProperty(0)
            val manualSize = SimpleIntegerProperty(0)
            val value: ObjectProperty<Int> = SimpleObjectProperty(0)

            init {
                updateMessage("")
                updateProgress(0, 100)
                setOnSucceeded { evt -> updateProgress(100, 100) }
            }

            private fun appendValue(var1: String) {
                if (Platform.isFxApplicationThread()) {
                    valueList.add(var1)
                } else {
                    val sTime = LocalTime.now()
                    synchronized(dataList) {
                        if (dataList.isEmpty()) {
                            Platform.runLater {
                                val sTime = LocalTime.now()
                                val tempList = mutableListOf<String>()
                                synchronized(dataList) {
                                    tempList.addAll(dataList)
                                    dataList.clear()
                                    valueList.add(
                                        "FX Event ${counter.value++} step 1 in ${
                                            sTime.until(
                                                LocalTime.now(),
                                                ChronoUnit.MILLIS
                                            )
                                        }ms"
                                    )
                                }
                                valueList.addAll(tempList)
                                valueList.add(
                                    "FX Event ${counter.value++} done 1 in ${
                                        sTime.until(
                                            LocalTime.now(),
                                            ChronoUnit.MILLIS
                                        )
                                    }ms\n"
                                )
                            }
                        }
                        dataList.add(var1 + " ${sTime.until(LocalTime.now(), ChronoUnit.MILLIS)}")
                    }
                }
            }

            override fun call(): Int {
                handleProcessing3(
                    { done, total -> updateProgress(done, total) },
                    { appendValue(it) },
                    { updateMessage(it) })
                while (!dataList.isEmpty()) {
                    updateMessage("Waiting for completion: ${dataList.size}")
                    println("Waiting for completion: ${dataList.size}")
                    Thread.sleep(10)
                }
                Thread.sleep(100)
                updateMessage("All jobs completed")
                val tempSize = valueList.size
                Platform.runLater { manualSize.value = tempSize }
                return valueList.size
            }
        }
        center = VBox(10.0).apply {
            children += HBox(
                10.0,
                promptOf("Manual Size:"),
                Label().apply { textProperty().bind(task.manualSize.asString()) })
            children += HBox(
                10.0,
                promptOf("Counted Size:"),
                Label().apply { textProperty().bind(task.valueList.sizeProperty().asString()) })
            children += HBox(
                10.0,
                promptOf("Task Value:"),
                Label().apply { textProperty().bind(task.valueProperty().asString()) })
            children += HBox(
                10.0,
                promptOf("Task Completion:"),
                Label("None").apply {
                    task.setOnSucceeded { evt -> this.text = task.get().toString() }
                })
            children += Label().apply {
                textProperty().bind(task.messageProperty())
            }
            children += ProgressBar().apply {
                progressProperty().bind(task.progressProperty())
                minWidth = 200.0
            }
            alignment = Pos.CENTER
        }
        right = TextArea().apply {
            textProperty().bind(ListToString(task.valueList))
        }
        bottom = HBox(Button("Run Task").apply {
            setOnAction { Thread(task).start() }
        }).apply {
            alignment = Pos.CENTER
            padding = Insets(20.0)
        }
    }
}

fun handleProcessing3(
    progressUpdater: (Long, Long) -> Unit,
    dataCollector: (String) -> Unit,
    messageUpdater: (String) -> Unit
) {
    val job = Runnable {
        dataCollector("Thread ${Thread.currentThread().name} starting")
        for (x in 0..1000) {
            dataCollector("Thread ${Thread.currentThread().name}: $x")
            Thread.sleep(Duration.ofMillis(4))
        }
    }
    val sTime = LocalTime.now()
    progressUpdater(10, 100)
    messageUpdater("Initializing")
    Thread.sleep(Duration.ofMillis(1000))
    progressUpdater(-1, 100)
    messageUpdater("Processing")
    val threadPool = Executors.newFixedThreadPool(50)
    repeat(50) {
        threadPool.execute(job)
    }
    messageUpdater("ThreadPool Started")
    threadPool.shutdown()
    var counter = 0
    while (!threadPool.awaitTermination(4, TimeUnit.SECONDS))
        messageUpdater("Still Waiting ${counter++}")
    messageUpdater("Completed ${sTime.until(LocalTime.now(), ChronoUnit.MILLIS)}")
    progressUpdater(100, 100)
}


fun main() {
    Application.launch(TaskProgress6App::class.java)
}