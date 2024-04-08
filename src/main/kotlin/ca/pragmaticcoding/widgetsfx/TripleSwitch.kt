package ca.pragmaticcoding.widgetsfx

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import javafx.scene.control.Control
import javafx.scene.control.Skin
import org.kordamp.ikonli.javafx.FontIcon

class TripleSwitch(val labelLeft: String, val labelCentre: String, val labelRight: String) : Control() {

   private val _value: ObjectProperty<ToggleSelection> = SimpleObjectProperty(ToggleSelection.CENTRE)
   var value: ToggleSelection
      get() = _value.get()
      set(newValue) = _value.set(newValue)

   private val _leftIcon: ObjectProperty<Node> = SimpleObjectProperty(FontIcon("captainicon-146"))
   var leftIcon: Node
      get() = _leftIcon.get()
      set(value) = _leftIcon.set(value)
   private val _centreIcon: ObjectProperty<Node> = SimpleObjectProperty(FontIcon("captainicon-150"))
   var centreIcon: Node
      get() = _centreIcon.get()
      set(value) = _centreIcon.set(value)
   private val _rightIcon: ObjectProperty<Node> = SimpleObjectProperty(FontIcon("captainicon-158"))
   var rightIcon: Node
      get() = _rightIcon.get()
      set(value) = _rightIcon.set(value)

   fun leftIconProperty() = _leftIcon
   fun centreIconProperty() = _centreIcon
   fun rightIconProperty() = _rightIcon
   fun valueProperty() = _value

   init {
     
   }

   override fun createDefaultSkin(): Skin<*> = TripleSwitchSkin(this)

   enum class ToggleSelection {
      LEFT, CENTRE, RIGHT
   }
}

