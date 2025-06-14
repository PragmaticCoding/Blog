package ca.pragmaticcoding.blog.conditionalbinding

import javafx.application.Application
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage

class ComboBoxTest1 : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 400.0, 400.0)
        stage.show()
    }

    private fun createContent(): Region = VBox(20.0).apply {
        val model = Model()
        children += Label().apply {
            textProperty().bind(model.testValue.map { "The current value in the ComboBox: $it" })
        }
        children += ComboBox(model.valueList).apply {
            model.testValue.bind(valueProperty())
        }
        padding = Insets(40.0)
    }
}


class Model {
    val testValue = SimpleObjectProperty<String>("")
    val valueList: ObservableList<String> =
        FXCollections.observableArrayList("abc", "abj", "def", "hij", "mno", "xyz", "123", "456")
}


fun main() = Application.launch(ComboBoxTest1::class.java)