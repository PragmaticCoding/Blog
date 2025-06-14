package ca.pragmaticcoding.blog.observablelist

import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Region
import javafx.stage.Stage

class ObListExample1 : Application() {

    val obList: ObservableList<Int> = FXCollections.observableArrayList()
    val messages: StringProperty = SimpleStringProperty("")

    override fun start(stage: Stage) {
        obList.subscribe { messages.value += "Invalidated \n" }
        stage.scene = Scene(createContent()).apply { }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
//        center = ListView<Int>().apply {
//            items = obList
//        }
        center = TextArea().apply {
            textProperty().bind(messages)
        }
        bottom = Button("Add Item").apply {
            setOnAction { obList.add(3) }
        }
        padding = Insets(20.0)
    }
}

fun main() = Application.launch(ObListExample1::class.java)