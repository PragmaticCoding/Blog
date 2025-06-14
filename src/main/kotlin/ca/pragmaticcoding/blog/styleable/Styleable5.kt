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

class Styleable5 : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent()).apply {
            Styleable3::class.java.getResource("styleable3.css")?.toString()?.let { stylesheets += it }
        }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        center = customBox()
        padding = Insets(10.0)
    }

    private fun customBox(): Region = StyleableHBox(20.0).apply {
        val circleColour =
            StandardStyleableObjectProperty("-wfx-circle-colour", StyleConverter.getColorConverter(), Color.BLUEVIOLET)
        val squareColour =
            StandardStyleableObjectProperty("-wfx-square-colour", StyleConverter.getColorConverter(), Color.BLUEVIOLET)
        layoutCssMetaData += listOf(circleColour.cssMetaData, squareColour.cssMetaData)
        styleClass += "custom-widget"
        children += Circle(100.0).apply {
            fillProperty().bind(circleColour)
        }
        children += Rectangle(200.0, 200.0).apply {
            fillProperty().bind(squareColour)
        }
    }
}

class StyleableHBox(spacing: Double) : HBox(spacing) {
    val layoutCssMetaData = mutableListOf<CssMetaData<out Styleable, *>>()

    override fun getCssMetaData(): MutableList<CssMetaData<out Styleable, *>> {
        return (super.getCssMetaData() + layoutCssMetaData) as MutableList
    }
}

fun main() = Application.launch(Styleable5::class.java)