package ca.pragmaticcoding.blog.styleable

import javafx.application.Application
import javafx.css.CssMetaData
import javafx.css.StyleConverter
import javafx.css.Styleable
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.stage.Stage

class Styleable4 : Application() {
    private val circleColour =
        StandardStyleableObjectProperty("-wfx-circle-colour", StyleConverter.getColorConverter(), Color.BLUEVIOLET)
    private val squareColour =
        StandardStyleableObjectProperty("-wfx-square-colour", StyleConverter.getColorConverter(), Color.BLUEVIOLET)

    override fun start(stage: Stage) {
        stage.scene = Scene(createContent()).apply {
            Styleable3::class.java.getResource("styleable3.css")?.toString()?.let { stylesheets += it }
        }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        center = customBox3()
        padding = Insets(10.0)
    }

    private fun customBox3(): Region = object : HBox(20.0) {
        override fun getCssMetaData(): MutableList<CssMetaData<out Styleable, *>> =
            (getClassCssMetaData() + circleColour.cssMetaData + squareColour.cssMetaData) as MutableList
    }.apply {
        styleClass += "custom-widget"
        children += Circle(100.0).apply {
            fillProperty().bind(circleColour)
        }
        children += Rectangle(200.0, 200.0).apply {
            fillProperty().bind(squareColour)
        }
    }
}

fun main() = Application.launch(Styleable4::class.java)