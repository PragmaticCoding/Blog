package ca.pragmaticcoding.blog.styleable

import javafx.application.Application
import javafx.css.CssMetaData
import javafx.css.StyleConverter
import javafx.css.Styleable
import javafx.css.StyleableProperty
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.stage.Stage

class Styleable3 : Application() {
    private val circleColour = DeferredStyleableObjectProperty(Color.BLUEVIOLET)
    private val circleColourMetaData: CssMetaData<Styleable, Color> =
        object : CssMetaData<Styleable, Color>("-wfx-circle-colour", StyleConverter.getColorConverter()) {
            override fun isSettable(p0: Styleable): Boolean = !circleColour.isBound
            override fun getStyleableProperty(p0: Styleable): StyleableProperty<Color> = circleColour

            init {
                circleColour.setCssMetaData(this)
            }
        }

    private val squareColour = DeferredStyleableObjectProperty(Color.BLUEVIOLET)
    private val squareColourMetaData: CssMetaData<Styleable, Color> =
        object : CssMetaData<Styleable, Color>("-wfx-square-colour", StyleConverter.getColorConverter()) {
            override fun isSettable(p0: Styleable): Boolean = !squareColour.isBound
            override fun getStyleableProperty(p0: Styleable): StyleableProperty<Color> = squareColour

            init {
                squareColour.setCssMetaData(this)
            }
        }

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
            (getClassCssMetaData() + circleColourMetaData + squareColourMetaData) as MutableList
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

fun main() = Application.launch(Styleable3::class.java)