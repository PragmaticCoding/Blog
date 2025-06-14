package ca.pragmaticcoding.blog.observablelist

import javafx.application.Application
import javafx.beans.binding.IntegerBinding
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
import javafx.scene.layout.Region
import javafx.stage.Stage

class ObListExample2 : Application() {

    private val obList: ObservableList<Int> = FXCollections.observableArrayList()
    private val messages: StringProperty = SimpleStringProperty("")
    private val totBinding = object : IntegerBinding() {
        init {
            super.bind(obList)
        }

        override fun computeValue(): Int = obList.sum()
    }

    override fun start(stage: Stage) {
        obList.subscribe { messages.value += "Invalidated \n" }
        stage.scene = Scene(createContent()).apply { }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        top = Label().apply { textProperty().bind(totBinding.asString()) }
        center = TextArea().apply {
            textProperty().bind(messages)
        }
        bottom = Button("Add Item").apply {
            setOnAction { obList.add(3) }
        }
        padding = Insets(20.0)
    }
}


fun main() = Application.launch(ObListExample2::class.java)