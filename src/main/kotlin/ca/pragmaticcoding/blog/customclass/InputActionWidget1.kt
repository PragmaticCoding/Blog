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
import javafx.util.Duration

class InputActionWidget1(private val boundProperty: StringProperty,
                         labelText: String = "Prompt:",
                         buttonText: String = "Start") : Region() {

   private var buttonAction: ButtonRunner = {}
   private val actionRunning: BooleanProperty = SimpleBooleanProperty(false)
   private val hBox: HBox
   private val label = Label(labelText)
   private val textField = TextField()
   private val button = Button(buttonText)
   var labelText: String
      get() = label.text
      set(value) = run { label.text = value }
   val labelTextProperty: StringProperty
      get() = label.textProperty()
   var buttonText: String
      get() = button.text
      set(value) = run { button.text = value }
   val buttonTextProperty: StringProperty
      get() = button.textProperty()
   var textFieldText: String
      get() = textField.text
      set(value) = run { textField.text = value }
   val textFieldTextProperty: StringProperty
      get() = textField.textProperty()

   init {
      hBox = createLayout()
      children.add(hBox)
   }

   private fun createLayout() = HBox().apply {
      children += listOf(label, textField, button)
      textFieldTextProperty.bindBidirectional(boundProperty)
      configureButton()
      alignment = Pos.CENTER_LEFT
   }

   private fun configureButton() {
      button.apply {
         onAction = EventHandler<ActionEvent> {
            actionRunning.value = true
            buttonAction { actionRunning.value = false }
         }
         defaultButtonProperty().bind(textField.focusedProperty().and(textField.textProperty().isNotEmpty))
         disableProperty().bind((textField.textProperty().isEmpty).or(actionRunning))
      }
   }

   fun setButtonAction(newAction: ButtonRunner) = this.apply {
      buttonAction = newAction
   }

   fun withSpacing(newSpacing: Double) = this.apply { hBox.spacing = newSpacing }
}

class RegionStyle2 : Application() {

   private val name: StringProperty = SimpleStringProperty("")
   private val results: StringProperty = SimpleStringProperty("No search done")

   override fun start(stage: Stage) {
      stage.scene = Scene(createContent())
      stage.show()
   }

   private fun createContent(): Region = BorderPane().apply {
      center = InputActionWidget1(name).setButtonAction { postRunAction ->
         doSearch()
         PauseTransition(Duration(3000.0)).apply {
            onFinished = EventHandler { postRunAction.run() }
            play()
         }
      }.withSpacing(50.0).apply {
         labelText = "Last Name:"
         buttonText = "Look For Them"
      }
      bottom = Label().apply { textProperty().bind(results) }
      padding = Insets(20.0)
   }

   private fun doSearch() {
      results.value = "Nothing found for: ${name.value}"
   }
}

fun main() = Application.launch(RegionStyle2::class.java)