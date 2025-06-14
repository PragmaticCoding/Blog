package ca.pragmaticcoding.blog.conditionalbinding

import javafx.application.Application
import javafx.beans.binding.ObjectBinding
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage

class ComboBoxTest4 : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 400.0, 400.0)
        stage.show()
    }

    private fun createContent(): Region = VBox(20.0).apply {
        val model = Model4()
        children += Label().apply {
            textProperty().bind(model.testValue.map { "The current value in the ComboBox: $it" })
        }
        children += ComboBox(model.valueList).apply {
            model.testValue.bind(ConditionalBinding(valueProperty(), showingProperty().not()))
        }
        padding = Insets(40.0)
    }
}

class ConditionalBinding<T>(
    private val dependency: ObservableValue<T?>,
    private val condition: ObservableBooleanValue
) : ObjectBinding<T?>() {

    init {
        super.bind(dependency, condition)
    }

    private var oldValue: T? = null

    override fun computeValue(): T? {
        if (condition.value) {
            oldValue = dependency.value
        }
        return oldValue
    }
}


class Model4 {
    val testValue = SimpleObjectProperty<String>("")
    val valueList: ObservableList<String> =
        FXCollections.observableArrayList("abc", "abj", "def", "hij", "mno", "xyz", "123", "456")
}


fun main() = Application.launch(ComboBoxTest4::class.java)