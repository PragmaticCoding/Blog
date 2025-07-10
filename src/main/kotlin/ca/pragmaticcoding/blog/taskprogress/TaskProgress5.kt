package ca.pragmaticcoding.blog.taskprogress

import javafx.application.Application
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
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
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.decrementAndFetch
import kotlin.concurrent.atomics.incrementAndFetch

class TaskProgress5App : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 800.0, 500.0)
        stage.title = "Task Progress Example"
        stage.show()
    }

    @OptIn(ExperimentalAtomicApi::class)
    private fun createContent(): Region = BorderPane().apply {
        val task = object : Task<Unit>() {

            val valueList: ObservableList<String> = FXCollections.observableArrayList()
            val waitingEvents = AtomicInt(0)

            init {
                updateMessage("")
                updateProgress(0, 100)
                setOnSucceeded { evt -> updateProgress(100, 100) }
            }

            private fun appendValue(var1: String) {
                if (Platform.isFxApplicationThread()) {
                    valueList.add(var1)
                } else {
                    println("Queuing: ${waitingEvents.incrementAndFetch()}")
                    Platform.runLater {
                        println("Performing: ${waitingEvents.decrementAndFetch()}")
                        valueList.add(var1)
                    }
                }
            }

            override fun call() {
                handleProcessing2(
                    { done, total -> updateProgress(done, total) },
                    { appendValue(it) },
                    { updateMessage(it) })
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

@OptIn(ExperimentalAtomicApi::class)
fun handleProcessing2(
    progressUpdater: (Long, Long) -> Unit,
    dataCollector: (String) -> Unit,
    messageUpdater: (String) -> Unit
) {
    val maxCount = 1000
    val threadCount = 50
    val job = Runnable {
        dataCollector("Thread ${Thread.currentThread().name} starting")
        for (x in 0..maxCount) {
            dataCollector("Thread ${Thread.currentThread().name}: $x")
            Thread.sleep(Duration.ofMillis(10))
        }
    }
    val sTime = LocalTime.now()

    progressUpdater(0, 100)
    messageUpdater("Initializing")
    Thread.sleep(Duration.ofMillis(1000))
    progressUpdater(-1, 100)
    messageUpdater("Processing")
    val threadPool = Executors.newFixedThreadPool(threadCount)
    repeat(threadCount) {
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
    Application.launch(TaskProgress5App::class.java)
}