package ca.pragmaticcoding.blog.beginners

import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.*
import javafx.stage.Stage

class Opening : Application() {
    private val isShowingNewGame: BooleanProperty = SimpleBooleanProperty(false)

    override fun start(primaryStage: Stage) {
        primaryStage.scene = Scene(createContent(), 500.0, 500.0).apply {
            Opening::class.java.getResource("beginners.css")?.toString()?.let { stylesheets += it }
        }
        primaryStage.title = "love u <3"
        primaryStage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        top = HBox(Label("Basil's Cat Game :3") styleWith "title-label").centred()
        bottom = HBox(createButton("EXIT") { Platform.exit() }).centred()
        center = StackPane(buttonBox() showWhen isShowingNewGame.not(), newGameBox() showWhen isShowingNewGame)
    }

    private fun buttonBox() =
        VBox(20.0, createButton("NEW") { isShowingNewGame.value = true }, createButton("LOAD") {}).centred()

    private fun newGameBox(): Region = BorderPane().apply {
        center = VBox(
            HBox(6.0, Label("Name:") styleWith "prompt-label", TextField(), Button("OK")).centred()
        ).centred()
        top = Button("Back").apply { setOnAction { isShowingNewGame.value = false } } styleWith "transparent-button"
    }

    private fun createButton(title: String, action: EventHandler<ActionEvent>) = Button(title).apply {
        onAction = action
        styleWith("transparent-button")
    }
}

infix fun <T : Node> T.styleWith(selector: String): T = apply { styleClass += selector }
infix fun <T : Node> T.showWhen(observed: ObservableBooleanValue): T = apply {
    visibleProperty().bind(observed)
    managedProperty().bind(observed)
}

fun HBox.centred() = apply { alignment = Pos.CENTER }
fun VBox.centred() = apply { alignment = Pos.CENTER }


fun main() {
    Application.launch(Opening::class.java)
}