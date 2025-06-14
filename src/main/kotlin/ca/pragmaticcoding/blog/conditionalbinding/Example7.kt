package ca.pragmaticcoding.blog.conditionalbinding

import javafx.application.Application
import javafx.beans.binding.IntegerBinding
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableObjectValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage

class ComboBoxTest7 : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 400.0, 400.0)
        stage.show()
    }

    private fun createContent(): Region = VBox(20.0).apply {
        val model = Model7()
        val countingBinding = CountingBinding2(model.testValue)
        children += Label().apply {
            textProperty().bind(model.testValue.map { "The current value in the ComboBox: $it" })
        }
        children += Label().apply {
            textProperty().bind(countingBinding.map { "Has changed $it times" })
        }
        children += Button("Reset").apply {
            setOnAction { countingBinding.reset() }
        }
        children += ComboBox(model.valueList) conditionallyBind model.testValue
        padding = Insets(40.0)
    }
}

class CountingBinding2<T>(private val dependency: ObservableObjectValue<T?>) : IntegerBinding() {

    init {
        super.bind(dependency)
    }

    fun reset() {
        counter = 0
        invalidate()
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

class Model7 {
    val testValue = SimpleObjectProperty<String>()
    val valueList: ObservableList<String> =
        FXCollections.observableArrayList("abc", "abj", "def", "hij", "mno", "xyz", "123", "456")
}


fun main() = Application.launch(ComboBoxTest7::class.java)