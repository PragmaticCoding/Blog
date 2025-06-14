package ca.pragmaticcoding.blog.modena

import ca.pragmaticcoding.widgetsfx.hoverLookup
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage


class ModenaExample1 : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 300.0, 240.0).apply {
            ModenaExample1::class.java.getResource("example1.css")?.toString()?.let { stylesheets += it }
        }
        stage.show()
    }

    private fun createContent(): Region = VBox(10.0).apply {
        styleClass += "nest-outer"
        children += Button("Outside")
        children += VBox(10.0).apply {
            styleClass += "nest-middle"
            children += Button("Middle").apply {
                onAction = EventHandler {
                    lookupAll("*").forEach {
                        println("Id: ${it.id}\tStyleClass: ${it.styleClass}\t\tType: ${it::class.java.simpleName}")
                    }
                }
                children += Label("Middle").hoverLookup()
                children += VBox(10.0).apply {
                    styleClass += "nest-inner"
                    children += Button("Inside")
                    children += Spinner<Int>()
                }
            }
        }
    }
}

fun main() = Application.launch(ModenaExample1::class.java)