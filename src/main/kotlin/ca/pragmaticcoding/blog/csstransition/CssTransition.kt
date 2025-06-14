package ca.pragmaticcoding.blog.csstransition

import javafx.application.Application
import javafx.beans.property.BooleanProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.css.PseudoClass
//import javafx.css.TransitionEvent
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Region
import javafx.stage.Stage

class CssTransitionExample : Application() {
    private val activatedPC = PseudoClass.getPseudoClass("activated")
    private val activated: BooleanProperty = SimpleBooleanProperty(false)
    private val transitionCounter: IntegerProperty = SimpleIntegerProperty(0)
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 340.0, 200.0).apply {
            CssTransitionExample::class.java.getResource("example.css")?.toString()?.let { stylesheets += it }
        }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        center = Label("This is the Label").apply {
            styleClass += "transition-label"
            activated.subscribe { newVal -> pseudoClassStateChanged(activatedPC, newVal) }
//            this.addEventHandler(TransitionEvent.START) { evt ->
//                transitionCounter.value += 1
//                println("Started: ${evt.property}")
//            }
//            this.addEventHandler(TransitionEvent.END) { transitionCounter.value -= 1 }
        }
        bottom = CheckBox("Activate PseudoClass").apply {
            selectedProperty().bindBidirectional(activated)
            disableProperty().bind(transitionCounter.greaterThan(0))
        }
        padding = Insets(40.0)
        val fred = PropertyValueFactory<String, String>("Abc")
    }
}

fun main() = Application.launch(CssTransitionExample::class.java)