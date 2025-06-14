package ca.pragmaticcoding.blog.conditionalbinding

import javafx.application.Application
import javafx.beans.binding.IntegerBinding
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableObjectValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage

class ComboBoxTest6 : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 400.0, 400.0)
        stage.show()
    }

    private fun createContent(): Region = VBox(20.0).apply {
        val model = Model6()
        children += Label().apply {
            textProperty().bind(model.testValue.map { "The current value in the ComboBox: $it" })
        }
        children += Label().apply {
            textProperty().bind(CountingBinding(model.testValue).map { "Has changed $it times" })
        }
        children += ComboBox(model.valueList) conditionallyBind model.testValue
        padding = Insets(40.0)
    }
}

class CountingBinding<T>(private val dependency: ObservableObjectValue<T?>) : IntegerBinding() {

    init {
        super.bind(dependency)
    }

    private var oldValue: T? = dependency.value
    private var counter = 0

    override fun computeValue(): Int {
        if (oldValue != dependency.value) {
            oldValue = dependency.value
            counter++
        }
        return counter
    }
}

class Model6 {
    val testValue = SimpleObjectProperty<String>()
    val valueList: ObservableList<String> =
        FXCollections.observableArrayList("abc", "abj", "def", "hij", "mno", "xyz", "123", "456")
}


fun main() = Application.launch(ComboBoxTest6::class.java)