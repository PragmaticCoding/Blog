package ca.pragmaticcoding.blog.combobox

import ca.pragmaticcoding.widgetsfx.addWidgetStyles
import javafx.application.Application
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.util.Pair
import java.util.function.Consumer


class ComboBoxExample3 : Application() {

    private val comboBoxValue: ObjectProperty<Pair<String, String>> = SimpleObjectProperty()

    override fun start(stage: Stage) {
        val scene = Scene(createContent(), 280.0, 300.0).apply {
            addWidgetStyles()
        }
        stage.scene = scene
        stage.show()
    }

    private fun createContent(): Region = VBox(20.0).apply {
        children += Label().apply {
            textProperty().bind(comboBoxValue.map { "The selected value is: ${it.key}" }
                .orElse("No value has been selected"))
        }
        children += ComboBox<Pair<String, String>>().apply {
            items += listOf(
                Pair("GB", "Great Britain"),
                Pair("FR", "France"),
                Pair("AU", "Australia"),
                Pair("ES", "Spain")
            )
            comboBoxValue.bind(valueProperty())
            setCellFactory { ListCell<Pair<String, String>>().apply { textProperty().bind(itemProperty().map { it.value }) } }
            buttonCell = ListCell<Pair<String, String>>().apply {
                itemProperty().subscribe(Consumer { text = it?.value })
            }
        }
        padding = Insets(40.0)
    }
}


fun main() = Application.launch(ComboBoxExample3::class.java)