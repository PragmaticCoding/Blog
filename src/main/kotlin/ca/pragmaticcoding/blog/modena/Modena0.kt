package ca.pragmaticcoding.blog.modena

import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.stage.Stage


class ModenaExample0 : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent()).apply {
            ModenaExample0::class.java.getResource("example0.css")?.toString()?.let { stylesheets += it }
        }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        val checkBox = CheckBox().apply {
            styleClass += "fred"
        }
        center = HBox(10.0).apply {
            children += Button("Button").apply {
                isDefaultButton = false
                onAction = EventHandler {
                    println("All of it: ${checkBox.lookupAll("*")}\n\n")
                    checkBox.lookupAll("*").forEach {
                        println("Id: ${it.id}\tStyleClass: ${it.styleClass}\t\tType: ${it::class.java.simpleName}")
                    }
                }
            }
            children += checkBox
        }
        padding = Insets(30.0)
    }
}

fun main() = Application.launch(ModenaExample0::class.java)