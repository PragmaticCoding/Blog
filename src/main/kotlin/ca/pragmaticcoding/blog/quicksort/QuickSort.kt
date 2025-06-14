package ca.pragmaticcoding.blog.quicksort

import ca.pragmaticcoding.widgetsfx.addWidgetStyles
import javafx.animation.Transition
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import javafx.concurrent.Task
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.Duration
import java.util.concurrent.CountDownLatch
import kotlin.math.abs
import kotlin.random.Random

class ElementModel(initialValue: Int, initialPosition: Int) {
    val number = SimpleIntegerProperty(initialValue)
    val position: IntegerProperty = SimpleIntegerProperty(initialPosition)
    val offset: DoubleProperty = SimpleDoubleProperty(0.0)
    val dip: DoubleProperty = SimpleDoubleProperty(0.0)
}

class QuickSort : Application() {

    private val mainAPane = Pane()
    private var array = IntArray(20) { Random.nextInt(0, 100) }
    private val elements = FXCollections.observableArrayList<ElementModel>()
    private val labelWidth = 25.0

    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 600.0, 300.0).apply {
            addWidgetStyles()
            QuickSort::class.java.getResource("quicksort.css")?.toString()?.let { stylesheets += it }
        }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        padding = Insets(20.0)
        center = mainAPane
        bottom = Button("Start").apply {
            setOnAction {
                isDisable = true
                quickSort(array) { isDisable = false }
            }
        }
        val startColour = Color.YELLOW
        array.forEachIndexed { index, i ->
            val element = ElementModel(i, index)
            elements.add(element)
            mainAPane.children += Label().apply {
                textProperty().bind(element.number.asString())
                prefWidth = labelWidth - 2.0
                styleClass += "element-label"
                background =
                    Background(BackgroundFill(startColour.interpolate(Color.DARKTURQUOISE, index / 20.0), null, null))
                layoutXProperty().bind(element.position.multiply(labelWidth))
                translateXProperty().bind(element.offset)
                translateYProperty().bind(element.dip)
                viewOrderProperty().bind(element.offset.map { it -> if (it != 0.0) 0.0 else 1.0 })
            }
        }
    }

    private fun animateElements(i: Int, j: Int, latch: CountDownLatch) {
        if (i == j) dipElement(i, latch) else swapVisualElements(i, j, latch)
    }

    private fun swapVisualElements(i: Int, j: Int, latch: CountDownLatch) {
        val elementI = elements.first { it.position.value == i }
        val elementJ = elements.first { it.position.value == j }
        val transition = object : Transition() {
            init {
                cycleDuration = Duration.millis(abs(i - j).coerceAtMost(8) * 140.0)
                setOnFinished {
                    elementI.offset.value = 0.0
                    elementJ.offset.value = 0.0
                    elementI.position.value = j
                    elementJ.position.value = i
                    latch.countDown()
                }
            }

            override fun interpolate(frac: Double) {
                elementI.offset.value =
                    ((elementJ.position.value * labelWidth) - (elementI.position.value * labelWidth)) * frac
                elementJ.offset.value =
                    ((elementI.position.value * labelWidth) - (elementJ.position.value * labelWidth)) * frac
            }
        }
        transition.play()
    }

    private fun dipElement(i: Int, latch: CountDownLatch) {
        val elementI = elements.first { it.position.value == i }
        val transition = object : Transition() {
            init {
                cycleDuration = Duration.millis(250.0)
                isAutoReverse = true
                cycleCount = 2
                setOnFinished {
                    elementI.dip.value = 0.0
                    latch.countDown()
                }
            }

            override fun interpolate(frac: Double) {
                elementI.dip.value = 20.0 * frac
            }
        }
        transition.play()
    }


    private fun quickSort(arr: IntArray, whenDone: () -> Unit) {
        val task: Task<Void> = object : Task<Void>() {
            override fun call(): Void? {
                quickSortHelper(arr, 0, arr.size - 1)
                return null
            }
        }
        task.setOnSucceeded { whenDone.invoke() }
        Thread(task).apply { isDaemon = true }.start()
    }

    private fun quickSortHelper(arr: IntArray, low: Int, high: Int) {
        if (low < high) {
            val pi = partition(arr, low, high)
            quickSortHelper(arr, low, pi - 1)
            quickSortHelper(arr, pi + 1, high)
        }
    }

    private fun qSwapHelper(arr: IntArray, i: Int, j: Int) {
        val latch = CountDownLatch(1)
        Platform.runLater { animateElements(i, j, latch) }
        try {
            latch.await()
        } catch (`_`: Exception) {
        }

        val temp = arr[i]
        arr[i] = arr[j]
        arr[j] = temp
    }

    private fun partition(arr: IntArray, low: Int, high: Int): Int {
        val pivot = arr[high]

        var i = low - 1

        for (j in low until high) {
            if (arr[j] < pivot) {
                i++
                qSwapHelper(arr, i, j)
            }
        }
        qSwapHelper(arr, i + 1, high)
        return (i + 1)
    }
}

fun main() = Application.launch(QuickSort::class.java)