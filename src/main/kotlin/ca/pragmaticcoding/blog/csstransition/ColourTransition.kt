package ca.pragmaticcoding.blog.csstransition

import javafx.animation.Transition
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.Duration

class ColourTransitionExample : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 340.0, 200.0).apply {
            CssTransitionExample::class.java.getResource("example.css")?.toString()?.let { stylesheets += it }
        }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        center = Button("This is a Button").apply {
            style = "-fx-color: red"
            setOnAction {
                object : Transition() {
                    init {
                        cycleDuration = Duration.millis(2000.0)
                    }

                    override fun interpolate(p0: Double) {
                        style = "-fx-color: ${Color.RED.interpolate(Color.GREEN, p0).toHexString()}"
                    }
                }.play()
            }
        }
        padding = Insets(40.0)
    }
}

fun Color.toHexString(): String {
    val r = (Math.round(red * 255).toInt()) shl 24
    val g = (Math.round(green * 255).toInt()) shl 16
    val b = (Math.round(blue * 255).toInt()) shl 8
    val a = (Math.round(opacity * 255).toInt())
    return String.format("#%08X", (r + g + b + a))
}

fun main() = Application.launch(ColourTransitionExample::class.java)