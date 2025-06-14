package ca.pragmaticcoding.blog.styleable

import javafx.application.Application
import javafx.css.*
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.stage.Stage

class Styleable2 : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent()).apply {
            Styleable3::class.java.getResource("styleable2.css")?.toString()?.let { stylesheets += it }
        }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        center = CustomBox2()
        padding = Insets(10.0)
    }
}

class CustomBox2 : Region() {

    private val circleColour: StyleableObjectProperty<Color> =
        SimpleStyleableObjectProperty(CIRCLE_COLOUR_META_DATA, Color.WHITE)
    private val squareColour: StyleableObjectProperty<Color> =
        SimpleStyleableObjectProperty(SQUARE_COLOUR_META_DATA, Color.WHITE)

    companion object CssStuff {
        val CIRCLE_COLOUR_META_DATA: CssMetaData<CustomBox2, Color> =
            object : CssMetaData<CustomBox2, Color>("-wfx-circle-colour", StyleConverter.getColorConverter()) {
                override fun isSettable(p0: CustomBox2): Boolean = !p0.circleColour.isBound

                override fun getStyleableProperty(p0: CustomBox2): StyleableProperty<Color> = p0.circleColour
            }
        val SQUARE_COLOUR_META_DATA: CssMetaData<CustomBox2, Color> =
            object : CssMetaData<CustomBox2, Color>("-wfx-square-colour", StyleConverter.getColorConverter()) {
                override fun isSettable(p0: CustomBox2): Boolean = !p0.squareColour.isBound

                override fun getStyleableProperty(p0: CustomBox2): StyleableProperty<Color> = p0.squareColour
            }
    }

    override fun getCssMetaData() =
        (getClassCssMetaData() + CIRCLE_COLOUR_META_DATA + SQUARE_COLOUR_META_DATA) as MutableList

    init {

        styleClass += "custom-widget"
        children += HBox(20.0).apply {
            styleClass += "inner-box"
            children += Circle(100.0).apply {
                fillProperty().bind(circleColour)
            }
            children += Rectangle(200.0, 200.0).apply {
                fillProperty().bind(squareColour)
            }
        }
    }
}

fun main() = Application.launch(Styleable3::class.java)