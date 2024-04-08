package ca.pragmaticcoding.blog.newskin

import ca.pragmaticcoding.widgetsfx.addWidgetStyles
import ca.pragmaticcoding.widgetsfx.padWith
import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.ToggleButton
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.kordamp.ikonli.javafx.FontIcon

class ToggleFlipDemo : Application() {

    private val nameProperty: StringProperty = SimpleStringProperty("Not Started")
    private var counter: Int = 0

    override fun start(primaryStage: Stage) {
        primaryStage.scene = Scene(createContent()).addWidgetStyles().apply {
            object {}::class.java.getResource("/css/toggleflip.css")?.toString()?.let { stylesheets += it }
        }
        primaryStage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        val customControl1 = ToggleButton("Power").apply {
            graphic = FontIcon("typ-plug").apply {
                iconSize = 23
            }
            skin = ToggleFlipSkin(this)
        }
        val customControl2 = ToggleButton("Power").apply {
            skin = ToggleFlipSkin(this)
        }
        val customControl3 = ToggleButton("").apply {
            skin = ToggleFlipSkin(this)
            graphic = FontIcon("typ-plug").apply {
                iconSize = 23
            }
        }

        center = VBox(20.0, customControl1, customControl2, customControl3).apply {
            alignment = Pos.CENTER_LEFT
            padding = Insets(0.0, 0.0, 0.0, 100.0)
        }
        minWidth = 400.0

    } padWith 20.0
}

fun main() = Application.launch(ToggleFlipDemo::class.java)
