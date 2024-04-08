package ca.pragmaticcoding.widgetsfx

import javafx.beans.binding.Bindings
import javafx.beans.property.DoubleProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.value.ObservableValue
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.SkinBase
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox

class TripleSwitchSkin(control: TripleSwitch) : SkinBase<TripleSwitch>(control) {

   private val standardWidthProperty: DoubleProperty = SimpleDoubleProperty(0.0)
   private val theToggles = ToggleGroup()
   private val leftButton = createButton(control.leftIconProperty())
   private val centreButton = createButton(control.centreIconProperty())
   private val rightButton = createButton(control.rightIconProperty())

   init {
      children += createContent()
      control.valueProperty().bind(Bindings.createObjectBinding(this::checkValue, theToggles.selectedToggleProperty()))
      registerChangeListener(control.leftIconProperty()) { e: ObservableValue<*>? ->
         skinnable.requestLayout()
      }
   }

   private fun checkValue(): TripleSwitch.ToggleSelection {
      return when (theToggles.selectedToggle) {
         leftButton -> TripleSwitch.ToggleSelection.LEFT
         centreButton -> TripleSwitch.ToggleSelection.CENTRE
         rightButton -> TripleSwitch.ToggleSelection.RIGHT
         else -> TripleSwitch.ToggleSelection.CENTRE
      }
   }

   private fun createContent(): Region = VBox(10.0).apply {
      children += buttonBox()
      children += labelBox()
      isFillWidth = false
      alignment = Pos.CENTER
      centreButton.isSelected = true
   }

   private fun labelBox(): Region = HBox(20.0).apply {
      val labels =
         listOf(toggleLabel(skinnable.labelLeft), toggleLabel(skinnable.labelCentre), toggleLabel(skinnable.labelRight))
      children += labels
      standardWidthProperty.bind(Bindings.createDoubleBinding({ labels.maxOfOrNull { label -> label.width } },
                                                              *(labels
                                                                 .map { label -> label.widthProperty() }
                                                                 .toTypedArray())))
      labels.forEach { label -> label.minWidthProperty().bind(standardWidthProperty) }
      alignment = Pos.CENTER
   }

   private fun toggleLabel(labelText: String): Region = Label(labelText).apply {
      styleClass += "toggle-label"
      alignment = Pos.CENTER
   }

   private fun buttonBox(): Region = HBox(20.0).apply {
      children += listOf(leftButton, centreButton, rightButton)
      spacingProperty().bind(standardWidthProperty.subtract(20.0))
      styleClass += "button-box"
   }

   private fun createButton(iconName: ObjectProperty<Node>) = ToggleButton().apply {
      styleClass += "toggle-switch-button"
      theToggles.toggles += this
      graphicProperty().bind(iconName)
   }

}

