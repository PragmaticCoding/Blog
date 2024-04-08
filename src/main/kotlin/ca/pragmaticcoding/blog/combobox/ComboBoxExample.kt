package ca.pragmaticcoding.blog.combobox;

import ca.pragmaticcoding.widgetsfx.addWidgetStyles
import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage


class ComboBoxExample : Application() {

    private val comboBoxValue: StringProperty = SimpleStringProperty("")

    override fun start(stage: Stage) {
        val scene = Scene(createContent()).apply {
            addWidgetStyles()
        }
        stage.scene = scene
        stage.show()
    }

    private fun createContent(): Region = VBox(20.0).apply {
        children += Label().apply {
            textProperty().bind(comboBoxValue.map { "The selected value is: $it" }.orElse("No value has been selected"))
        }
        children += ComboBox<String>().apply {
            items += listOf("Fred", "George", "Mary", "Jane")
            comboBoxValue.bind(valueProperty())
        }
        padding = Insets(40.0)
    }
}

fun main() = Application.launch(ComboBoxExample::class.java)