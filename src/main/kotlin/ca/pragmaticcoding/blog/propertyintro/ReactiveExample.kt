package ca.pragmaticcoding.blog.propertyintro

import javafx.application.Application
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.css.PseudoClass
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage

class PresentationModel {
    val password: StringProperty = SimpleStringProperty("")
    val okToSave: BooleanProperty = SimpleBooleanProperty(false)

    init {
        okToSave.bind(password.map { it.length >= 8 })
    }
}

class ReactiveExample : Application() {
    private val model = PresentationModel()
    private val showWarning = model.okToSave.not().and(model.password.isNotEmpty)
    private val errorPC = PseudoClass.getPseudoClass("error")
    private val warningPC = PseudoClass.getPseudoClass("warning")
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 400.0, 300.0).apply {
            ReactiveExample::class.java.getResource("example.css")?.toString()?.let { stylesheets += it }
        }
        stage.show()
    }

    private fun createContent() = BorderPane().apply {
        center = createCentre()
        bottom = Button("Save").apply {
            disableProperty().bind(model.okToSave.not())
        }
        padding = Insets(40.0)
    }

    private fun createCentre(): Region = VBox(10.0).apply {
        children += HBox(6.0).apply {
            children += Label("New Password:")
            children += TextField().apply {
                textProperty().bindBidirectional(model.password)
            }
        }
        children += Label("New password must be at least 8 characters").apply {
            styleClass += "status-label"
            visibleProperty().bind(showWarning)
            model.password.map { ((it.isNotEmpty()) && (it.length < 5)) }
                .subscribe { newVal -> this.pseudoClassStateChanged(errorPC, newVal) }
            model.password.map { ((it.length >= 5) && (it.length < 8)) }
                .subscribe { newVal -> this.pseudoClassStateChanged(warningPC, newVal) }
        }
    }
}


fun main() = Application.launch(ReactiveExample::class.java)