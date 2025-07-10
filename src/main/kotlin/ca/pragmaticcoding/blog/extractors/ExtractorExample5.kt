package ca.pragmaticcoding.blog.extractors

import javafx.application.Application
import javafx.beans.InvalidationListener
import javafx.beans.binding.IntegerBinding
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.stage.Stage

class ExtractorExample5 : Application() {

    private val obList: ObservableList<ExampleData> =
        FXCollections.observableArrayList { item -> arrayOf(item.value2) }

    private val messages: StringProperty = SimpleStringProperty("")
    private val totBinding = object : IntegerBinding() {
        init {
            super.bind(obList)
        }

        override fun computeValue(): Int = obList.map { it.value2.value }.sum()
    }

    override fun start(stage: Stage) {
        obList.addListener(InvalidationListener { messages.value += "Invalidated \n" })
        for (x in 0..5) obList.add(ExampleData(x))
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
        left = TableView<ExampleData>().apply {
            items = obList
            columns += TableColumn<ExampleData, Int>("Value 1").apply {
                setCellValueFactory { p -> p.value.value1.asObject() }
            }
            columns += TableColumn<ExampleData, Int>("Value 2").apply {
                maxHeight = 100.0
                setCellValueFactory { p -> p.value.value2.asObject() }
                setCellFactory { column ->
                    object : TableCell<ExampleData, Int>() {
                        override fun updateItem(newItem: Int?, empty: Boolean) {
                            super.updateItem(newItem, empty)
                            messages.value += "Table Cell Update: $newItem\n"
                            text = null
                            graphic = null
                            newItem?.let {
                                if (!empty) {
                                    text = newItem.toString()
                                }
                            }
                        }
                    }
                }
            }
        }
        bottom = Button("Increment Item 2").apply {
            setOnAction {
                messages.value += "Button 1 Clicked\n"
                with(obList[2]) { value2.value = value2.value + 1 }
                messages.value += obList.map { it.value2.value }.joinToString() + "\n"
            }
        }
        padding = Insets(20.0)
    }
}


fun main() = Application.launch(ExtractorExample5::class.java)