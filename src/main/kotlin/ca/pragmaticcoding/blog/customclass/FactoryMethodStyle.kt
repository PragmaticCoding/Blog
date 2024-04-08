package ca.pragmaticcoding.blog.customclass

import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.stage.Stage

class FactoryMethodStyle : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent())
        stage.show()
    }

    private val name: StringProperty = SimpleStringProperty("")
    private val results: StringProperty = SimpleStringProperty("No search done")

    private fun createContent(): Region = BorderPane().apply {
        center = createCustomBox("Last Name:", "Search", name) { doSearch() }
        bottom = Label().apply { textProperty().bind(results) }
        padding = Insets(20.0)
    }

    private fun createCustomBox(
        labelText: String,
        buttonText: String,
        boundProperty: StringProperty,
        handler: EventHandler<ActionEvent>
    ): Region = HBox().apply {
        val textField = TextField().apply { textProperty().bindBidirectional(boundProperty) }
        children += Label(labelText)
        children += textField
        children += Button(buttonText).apply {
            onAction = handler
            defaultButtonProperty().bind(textField.focusedProperty().and(name.isNotEmpty))
            disableProperty().bind(name.isEmpty)
        }
        alignment = Pos.CENTER_LEFT
        spacing = 6.0
    }

    private fun doSearch() {
        results.value = "Nothing found for: ${name.value}"
    }
}

fun main() = Application.launch(FactoryMethodStyle::class.java)