package ca.pragmaticcoding.blog.extractors

import javafx.application.Application
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.beans.binding.IntegerBinding
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
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

class ExtractorExample4 : Application() {

    private val obList: ObservableList<ExampleData1> =
        FXCollections.observableArrayList({ item -> item.extractableValues() })

    private val messages: StringProperty = SimpleStringProperty("")
    private val totBinding = object : IntegerBinding() {
        init {
            super.bind(obList)
        }

        override fun computeValue(): Int = obList.map { it.value2.value }.sum()
    }

    override fun start(stage: Stage) {
        obList.addListener(InvalidationListener { messages.value += "Invalidated \n" })
        for (x in 0..5) obList.add(ExampleData1(x))
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
        bottom = HBox(
            20.0, Button("Increment Item 2").apply {
                setOnAction {
                    messages.value += "Button 1 Clicked\n"
                    with(obList[2]) { value2.value = value2.value + 1 }
                    messages.value += obList.map { it.value2.value }.joinToString() + "\n"
                }
            },
            Button("Increment Item 5").apply {
                setOnAction {
                    messages.value += "Button 2 Clicked\n"
                    with(obList[5]) { value2.value = value2.value + 1 }
                    messages.value += obList.map { it.value2.value }.joinToString() + "\n"
                }
            })
        padding = Insets(20.0)
    }
}

class ExampleData1(private val initialValue: Int) {
    val value1: IntegerProperty = SimpleIntegerProperty(initialValue)
    val value2: IntegerProperty = SimpleIntegerProperty(initialValue)

    override fun toString() = "[${value1.value}, ${value2.value}]"

    fun extractableValues(): Array<Observable> {
        return if (initialValue < 4) arrayOf(value1) else arrayOf(value2)
    }
}


fun main() = Application.launch(ExtractorExample4::class.java)