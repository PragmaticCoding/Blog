package ca.pragmaticcoding.blog.mandelbrot

import javafx.application.Application
import javafx.application.Platform
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.beans.binding.Bindings
import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.beans.value.ObservableStringValue
import javafx.concurrent.Task
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.*
import javafx.scene.image.WritableImage
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.converter.IntegerStringConverter
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class MyMandelbrot : Application() {
    private val width = 800.0
    private val height = 600.0
    private var maximumIterations = 50.0
    private var canvas = Canvas(width, height)
    private val zoom: DoubleProperty = SimpleDoubleProperty(250.0)
    private var xPos = -470.0
    private var yPos = 30.0
    private var hue = 264.0
    private var saturation = maximumIterations
    private var brightness = 0.9
    private var red = 60
    private var green = 0
    private var blue = 60
    private val setProgress: DoubleProperty = SimpleDoubleProperty(0.0)
    private val zoomIn: () -> Unit = { mandelbrotSet { zoom.value /= 0.4 } }
    private val zoomOut: () -> Unit = { mandelbrotSet { zoom.value *= 0.4 } }
    private val up: (Int) -> Unit = { mandelbrotSet { yPos -= height / zoom.value * it } }
    private val down: (Int) -> Unit = { mandelbrotSet { yPos += height / zoom.value * it } }
    private val left: (Int) -> Unit = { mandelbrotSet { xPos -= width / zoom.value * it } }
    private val right: (Int) -> Unit = { mandelbrotSet { xPos += width / zoom.value * it } }
    private val reset: () -> Unit = {
        mandelbrotSet {
            zoom.value = 250.0
            xPos = -470.0
            yPos = 30.0
        }
    }

    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), width, height)
        stage.title = "Mandelbrot Set"
        stage.show()
    }

    private fun createContent() = BorderPane().apply {
        top = mainMenuBar()
        center = canvas
        bottom = createBottom()
        minWidth = this@MyMandelbrot.width
        minHeight = this@MyMandelbrot.height
        addEventFilter(KeyEvent.KEY_PRESSED) {
            if (processKeystroke(it.code, it.isShiftDown)) {
                it.consume()
            }
        }
        with(canvas) {
            widthProperty().also {
                it.bind(this@apply.widthProperty())
                it.addListener(InvalidationListener {
                    mandelbrotSet()
                })
            }
            heightProperty().also {
                it.bind(this@apply.heightProperty().add(-60.0))
                it.addListener(InvalidationListener {
                    mandelbrotSet()
                })
            }
            onMouseClicked = EventHandler {
                when (it.button) {
                    MouseButton.PRIMARY -> zoomIn()
                    MouseButton.SECONDARY -> zoomOut()
                    else -> {}
                }
            }
        }
    }

    private fun processKeystroke(keyCode: KeyCode, isShiftDown: Boolean): Boolean {
        var didSomething = true
        when (keyCode) {
            KeyCode.W, KeyCode.UP -> up(if (isShiftDown) 10 else 100)
            KeyCode.A, KeyCode.LEFT -> left(if (isShiftDown) 10 else 100)
            KeyCode.S, KeyCode.DOWN -> down(if (isShiftDown) 10 else 100)
            KeyCode.D, KeyCode.RIGHT -> right(if (isShiftDown) 10 else 100)
            KeyCode.EQUALS -> zoomIn()
            KeyCode.MINUS -> zoomOut()
            KeyCode.SPACE -> reset()
            KeyCode.ESCAPE -> Platform.exit()
            else -> {
                didSomething = false
            }
        }
        return didSomething
    }

    private fun createBottom() = HBox(6.0).apply {
        children += listOf(
            Label("Iterations: "),
            iterationsBox(),
            ProgressBar().apply {
                progressProperty().bind(setProgress)
                prefWidth = 200.0
            },
            Label("      Pending: "),
            boundLabel(Bindings.createStringBinding({ pendingSets.value.toString() }, pendingSets)),
            Label("    Zoom:"),
            boundLabel(Bindings.createStringBinding({ zoom.value.roundToInt().toString() }, zoom))
        )
        padding = Insets(6.0)
    }

    private fun iterationsBox() = HBox(6.0).apply {
        val iterationsTextField = TextField()
        val textFormatter: TextFormatter<Int> = TextFormatter(IntegerStringConverter())
        iterationsTextField.textFormatter = textFormatter
        val refreshButton = Button("Refresh").apply {
            onAction = EventHandler {
                canvas.requestFocus()
                isDisable = true
                mandelbrotSet { maximumIterations = textFormatter.value?.toDouble() ?: 50.0 }
                isDisable = false
            }
            defaultButtonProperty().bind(iterationsTextField.focusedProperty())
        }
        children += listOf(iterationsTextField, refreshButton)
    }

    private fun boundLabel(contents: ObservableStringValue) = Label().apply { textProperty().bind(contents) }

    private fun iterationChecker(cr: Double, ci: Double): Int {
        var iterationsOfZ = 0
        var zr = 0.0
        var zi = 0.0
        while (iterationsOfZ < maximumIterations && ((zr.pow(2) + zi.pow(2)) < 4)) {
            val oldZr = zr
            zr = zr * zr - zi * zi + cr
            zi = 2 * (oldZr * zi) + ci
            iterationsOfZ++
        }
        return iterationsOfZ
    }

    private var taskRunning = false
    private val preProcesses = mutableListOf<() -> Unit>()
    private val pendingSets: IntegerProperty = SimpleIntegerProperty(0)

    private fun mandelbrotSet(preProcessor: () -> Unit = {}) {
        if ((canvas.width > 0) && (canvas.height > 0)) {
            preProcesses += preProcessor
            pendingSets.value = preProcesses.size
            println("In mandelbrot ${preProcesses.size} - already running?  $taskRunning")
            if (!taskRunning) {
                val startTime = System.currentTimeMillis()
                taskRunning = true
                preProcesses.forEach { it.invoke() }
                preProcesses.clear()
                pendingSets.value = 0
                val task = object : Task<WritableImage>() {
                    override fun call(): WritableImage =
                        performMandelbrot { workDone, maxWork -> updateProgress(workDone, maxWork) }
                }
                task.setOnSucceeded {
                    canvas.graphicsContext2D.drawImage(task.value, 0.0, 0.0)
                    taskRunning = false
                    if (preProcesses.isNotEmpty()) {
                        mandelbrotSet()
                    }
                }
                task.setOnFailed {
                    println("Failed! ${task.exception.stackTraceToString()}")
                    taskRunning = false
                }
                setProgress.unbind()
                setProgress.bind(task.progressProperty())
                Thread(task).start()
            }
        }
    }

    private fun performMandelbrot(progressConsumer: (workDone: Long, maxWork: Long) -> Unit) =
        WritableImage(canvas.width.toInt(), canvas.height.toInt()).apply {
            println("Starting mandelbrot")
            val centerY = width / 2.0
            val centerX = height / 2.0
            val counter = AtomicLong(0)
            (0 until width.roundToInt()).toList().parallelStream().forEach { x ->
                progressConsumer.invoke(counter.incrementAndGet(), width.roundToLong())
                for (y: Int in 0 until height.roundToInt()) {
                    val cr = xPos / width + (x - centerY) / zoom.value
                    val ci = yPos / height + (y - centerX) / zoom.value
                    pixelWriter.setColor(x, y, determineColour(iterationChecker(cr, ci)))
                }
            }
            println("Done")
        }

    private fun determineColour(iterations: Int): Color {
        if (iterations.toDouble() == maximumIterations) {  //inside the set
            return Color.rgb(red, green, blue)
        }
        if (brightness == 0.9) {  //white background
            return Color.hsb(hue, iterations / maximumIterations, brightness)
        }
        if (hue == 300.0) {  //colorful background
            return Color.hsb(hue * iterations / maximumIterations, saturation, brightness)
        }
        if (hue == 0.0 && saturation == 0.0 && brightness == 1.0) {
            return Color.hsb(hue, saturation, brightness)
        }
        return Color.hsb(hue, saturation, iterations / brightness)
    }

    private fun mainMenuBar() = MenuBar().apply {
        prefWidth = 176.0
        menus.addAll(menu1(), menu2())
    }

    @Suppress("UNCHECKED_CAST")
    private fun menu1() = Menu("Color").also {
        ToggleGroup().apply {
            toggles += listOf(makeColourMenuItem("Light", "1") {
                mandelbrotSet {
                    newSettings(246.0, maximumIterations, 0.9, 60, 0, 60)
                }
            },
                makeColourMenuItem("Dark", "2") {
                    mandelbrotSet {
                        newSettings(
                            0.0,
                            0.0,
                            maximumIterations,
                            15,
                            15,
                            15
                        )
                    }
                },
                makeColourMenuItem("Colourful", "3") { mandelbrotSet { newSettings(300.0, 1.0, 1.0, 35, 0, 35) } },
                makeColourMenuItem("Solid White", "4") { mandelbrotSet { newSettings(0.0, 0.0, 1.0, 0, 0, 0) } })
            it.items += toggles as List<MenuItem>
        }
    }

    private fun makeColourMenuItem(name: String, accel: String, func: () -> Unit) = RadioMenuItem(name).apply {
        selectedProperty().addListener { ob: Observable -> if ((ob as ObservableBooleanValue).value) func.invoke() }
        accelerator = KeyCombination.keyCombination(accel)
    }

    private fun menu2() = Menu("View").apply {
        items += listOf(makeMenuItem("Zoom In", "Plus") { zoomIn() },
            makeMenuItem("Zoom Out", "Minus") { zoomOut() },
            makeMenuItem("Reset", "Space") { reset() },
            makeMenuItem("Move Up", "UP") { up(100) },
            makeMenuItem("Move Down", "DOWN") { down(100) },
            makeMenuItem("Move Left", "LEFT") { left(100) },
            makeMenuItem("Move Right", "RIGHT") { right(100) })
    }

    private fun makeMenuItem(name: String, accel: String, eventHandler: EventHandler<ActionEvent>) =
        MenuItem(name).apply {
            accelerator = KeyCombination.keyCombination(accel)
            onAction = eventHandler
        }

    private fun newSettings(
        newHue: Double,
        newSat: Double,
        newBright: Double,
        newRed: Int,
        newGreen: Int,
        newBlue: Int
    ) {
        hue = newHue
        saturation = newSat
        brightness = newBright
        red = newRed
        green = newGreen
        blue = newBlue
    }
}

fun main() {
    Application.launch(MyMandelbrot::class.java)
}
