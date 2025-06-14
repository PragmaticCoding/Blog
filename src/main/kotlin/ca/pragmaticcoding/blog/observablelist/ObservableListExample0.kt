package ca.pragmaticcoding.blog.observablelist

import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.stage.Stage
import kotlin.random.Random

class ObListExample0 : Application() {

    private val obList: ObservableList<Int> = FXCollections.observableArrayList()
    private val messages: StringProperty = SimpleStringProperty("")

    override fun start(stage: Stage) {

        obList.addListener(ListChangeListener { change ->
            messages.value = "Change started...\n\n"
            var counter = 0
            while (change.next()) {
                messages.value += "Change element: ${counter++}\n"
                messages.value += "   From Index = ${change.from}\n"
                messages.value += "   To Index = ${change.to}\n\n"
                messages.value += "   wasAdded = ${change.wasAdded()}\n"
                messages.value += "   Added SubList = ${change.addedSubList}\n\n"
                messages.value += "   wasRemoved = ${change.wasRemoved()}\n"
                messages.value += "   Removed List = ${change.removed}\n\n"
                messages.value += "   wasPermutated = ${change.wasPermutated()}\n"
                if (change.wasPermutated()) {
                    messages.value += "   Permutated Indices = "
                    for (idx in change.from..<change.to) messages.value += " ${change.getPermutation(idx)}"
                    messages.value += "\n\n"
                }
                messages.value += "   wasUpdated = ${change.wasUpdated()}\n\n"
            }
        })
        stage.scene = Scene(createContent(), 480.0, 600.0).apply { }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        val listView = ListView<Int>().apply {
            items = obList
            selectionModel.selectionMode = SelectionMode.MULTIPLE
            prefWidth = 150.0
        }
        left = listView
        right = TextArea().apply {
            prefWidth = 280.0
            textProperty().bind(messages)
        }
        bottom = HBox(20.0).apply {
            children += Button("Add Item").apply {
                setOnAction {
                    obList.add(Random.nextInt(100))
                }
            }
            children += Button("Remove Item").apply {
                disableProperty().bind(listView.selectionModel.selectedItemProperty().isNull)
                setOnAction {
                    obList.removeAll(listView.selectionModel.selectedItems)
                }
            }
            children += Button("Sort").apply {
                setOnAction { obList.sort() }
            }
            children += Button("Change Item").apply {
                disableProperty().bind(listView.selectionModel.selectedItemProperty().isNull)
                setOnAction {
                    obList[listView.selectionModel.selectedIndex] = Random.nextInt(100)
                }
            }
            padding = Insets(10.0, 0.0, 0.0, 0.0)
        }
        padding = Insets(20.0)
    }
}

fun main() = Application.launch(ObListExample0::class.java)

