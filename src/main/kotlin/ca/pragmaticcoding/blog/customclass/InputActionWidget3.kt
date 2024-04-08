package ca.pragmaticcoding.blog.customclass

import javafx.animation.PauseTransition
import javafx.application.Application
import javafx.beans.InvalidationListener
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.css.*
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

class InputActionWidget3(private val boundProperty: StringProperty,
                         labelText: String = "Prompt:",
                         buttonText: String = "Start") : Region() {

   private var buttonAction: ButtonRunner = {}
   private val actionRunning: BooleanProperty = SimpleBooleanProperty(false)
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
   val tagGap: StyleableDoubleProperty = SimpleStyleableDoubleProperty(GAP_META_DATA, this, "tagGap")

   companion object CssStuff {
      val TEXTFIELD_FOCUSED: PseudoClass = PseudoClass.getPseudoClass("textfield-focused");
      val GAP_META_DATA: CssMetaData<InputActionWidget3, Number> =
         object : CssMetaData<InputActionWidget3, Number>("-wfx-gap", StyleConverter.getSizeConverter(), 6.0) {
            override fun isSettable(styleable: InputActionWidget3) = !styleable.tagGap.isBound
            override fun getStyleableProperty(styleable: InputActionWidget3) = styleable.tagGap
         }
      private val cssMetaDataList = (Region.getClassCssMetaData() + GAP_META_DATA) as MutableList
      fun getClassCssMetaData() = cssMetaDataList
   }

   override fun getCssMetaData() = getClassCssMetaData()

   init {
      styleClass += "input-action-widget"
      children.add(createLayout())
      println("Initial Tag Gap: $tagGap")
      tagGap.addListener(InvalidationListener { println("New Tag Gap: ${tagGap.value}") })
   }

   private fun createLayout() = HBox(4.0).apply {
      children += listOf(label, HBox(0.0, textField, button))
      textFieldTextProperty.bindBidirectional(boundProperty)
      configureButton()
      textField.focusedProperty().addListener { _, _, newValue ->
         button.pseudoClassStateChanged(TEXTFIELD_FOCUSED, newValue);
      }
      spacingProperty().bind(tagGap)
      alignment = Pos.CENTER_LEFT
   }

   private fun configureButton() {
      button.apply {
         onAction = EventHandler<ActionEvent> {
            actionRunning.value = true
            buttonAction { actionRunning.value = false }
         }
         isFocusTraversable = false
         defaultButtonProperty().bind(textField.focusedProperty().and(textField.textProperty().isNotEmpty))
         disableProperty().bind((textField.textProperty().isEmpty).or(actionRunning))
      }
   }

   fun setButtonAction(newAction: ButtonRunner) = this.apply {
      buttonAction = newAction
   }

}

class InputActionWidgetStyle3 : Application() {

   private val name: StringProperty = SimpleStringProperty("")
   private val results: StringProperty = SimpleStringProperty("No search done")

   override fun start(stage: Stage) {
      stage.scene = Scene(createContent()).apply {
         InputActionWidgetStyle3::class.java.getResource("/css/default.css")?.toString()?.let { stylesheets += it }
      }
      stage.show()
   }

   private fun createContent(): Region = BorderPane().apply {
      center = InputActionWidget3(name).setButtonAction { postRunAction ->
         doSearch()
         PauseTransition(Duration(3000.0)).apply {
            onFinished = EventHandler { postRunAction.run() }
            play()
         }
      }.apply {
         labelText = "Last Name:"
         buttonText = "Search"
      }
      bottom = HBox(10.0, Label().apply { textProperty().bind(results) }, Button("Test Button")).apply {
         padding = Insets(10.0)
      }
      padding = Insets(20.0)
   }

   private fun doSearch() {
      results.value = "Nothing found for: ${name.value}"
   }
}

fun main() = Application.launch(InputActionWidgetStyle3::class.java)