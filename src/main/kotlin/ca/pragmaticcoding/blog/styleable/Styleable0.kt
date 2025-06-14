package ca.pragmaticcoding.blog.styleable

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.stage.Stage

class Styleable0 : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent()).apply {
            Styleable3::class.java.getResource("styleable0.css")?.toString()?.let { stylesheets += it }
        }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        center = CustomBox()
        padding = Insets(10.0)
    }
}

class CustomBox : HBox(20.0) {

    init {
        styleClass += "custom-widget"
        children += Circle(100.0).apply {
            styleClass += "circle"
        }
        children += Rectangle(200.0, 200.0).apply {
            styleClass += "square"
        }
    }
}

fun main() = Application.launch(Styleable3::class.java)