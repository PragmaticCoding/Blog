package ca.pragmaticcoding.blog.extractors

import javafx.application.Application
import javafx.beans.InvalidationListener
import javafx.beans.binding.IntegerBinding
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.stage.Stage

class ExtractorExample3 : Application() {

    private val obList: ObservableList<ExampleData> =
        FXCollections.observableArrayList({ item -> arrayOf(item.value2) })

    private val messages: StringProperty = SimpleStringProperty("")
    private val totBinding = object : IntegerBinding() {
        init {
            super.bind(obList)
        }

        override fun computeValue(): Int = obList.map { it.value2.value }.sum()
    }

    override fun start(stage: Stage) {
        val listProperty = SimpleListProperty(obList)
        listProperty.addListener(InvalidationListener { messages.value += "Invalidated \n" })
        listProperty.subscribe { oldVal, newVal ->
            messages.value += "Change -> Old: $oldVal  New: $newVal\n"
        }
        for (x in 1..5) obList.add(ExampleData(x))
        messages.value += "Data Loaded\n"
        messages.value += obList.map { it.value2.value }.joinToString() + "\n"
        stage.scene = Scene(createContent()).apply { }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        top = HBox(
            10.0,
            Label().apply { textProperty().bind(totBinding.asString()) },
            Label().apply { textProperty().bind(obList[2].value2.asString()) })
        center = TextArea().apply {
            textProperty().bind(messages)
        }
        bottom = Button("Increment Item").apply {
            setOnAction {
                messages.value += "Button Clicked\n"
                with(obList[2]) { value2.value = value2.value + 1 }
                messages.value += obList.map { it.value2.value }.joinToString() + "\n"
            }
        }
        padding = Insets(20.0)
    }
}


fun main() = Application.launch(ExtractorExample3::class.java)