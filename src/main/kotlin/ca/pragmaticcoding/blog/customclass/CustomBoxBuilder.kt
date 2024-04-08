package ca.pragmaticcoding.blog.customclass

import javafx.animation.PauseTransition
import javafx.application.Application
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.stage.Stage
import javafx.util.Builder
import javafx.util.Duration

typealias ButtonRunner = (Runnable) -> Unit

class CustomBoxBuilder(private val labelText: String,
                       private val buttonText: String,
                       private val boundProperty: StringProperty) : Builder<Region> {
   private var spacing: Double = 4.0
   private var buttonAction: ButtonRunner = {}

   private val actionRunning: BooleanProperty = SimpleBooleanProperty(false)

   override fun build(): Region = HBox().apply {
      val textField = TextField().apply { textProperty().bindBidirectional(boundProperty) }
      children += Label(labelText)
      children += textField
      children += Button(buttonText).apply {
         onAction = EventHandler<ActionEvent> {
            actionRunning.value = true
            buttonAction { actionRunning.value = false }
         }
         defaultButtonProperty().bind(textField.focusedProperty().and(textField.textProperty().isNotEmpty))
         disableProperty().bind((textField.textProperty().isEmpty).or(actionRunning))
      }
      alignment = Pos.CENTER_LEFT
      spacing = this@CustomBoxBuilder.spacing
   }

   fun setSpacing(newValue: Double) = this.apply {
      spacing = newValue
   }

   fun setButtonAction(newAction: ButtonRunner) = this.apply {
      buttonAction = newAction
   }
}

class BuilderStyle : Application() {

   override fun start(stage: Stage) {
      stage.scene = Scene(createContent())
      stage.show()
   }

   private val name: StringProperty = SimpleStringProperty("")

   private val results: StringProperty = SimpleStringProperty("No search done")

   private fun createContent(): Region = BorderPane().apply {
      center = CustomBoxBuilder("Last Name:", "Search", name).setButtonAction { postRunAction ->
         doSearch()
         PauseTransition(Duration(3000.0)).apply {
            onFinished = EventHandler { postRunAction.run() }
            play()
         }
      }.setSpacing(50.0).build()
      bottom = Label().apply { textProperty().bind(results) }
      padding = Insets(20.0)
   }

   private fun doSearch() {
      results.value = "Nothing found for: ${name.value}"
   }
}

fun main() = Application.launch(BuilderStyle::class.java)