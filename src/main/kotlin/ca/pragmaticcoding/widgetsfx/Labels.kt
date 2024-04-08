package ca.pragmaticcoding.widgetsfx

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableStringValue
import javafx.scene.control.Label
import javafx.scene.control.Labeled

enum class LabelStyle(val selector: String) {
   PROMPT("label-prompt"), HEADING("label-heading")
}

infix fun <T : Labeled> T.styleAs(labelStyle: LabelStyle) = apply { styleClass += labelStyle.selector }

infix fun <T : Labeled> T.bindTo(value: ObservableStringValue) = apply { textProperty().bind(value) }

fun promptOf(value: ObservableStringValue) = Label() styleAs LabelStyle.PROMPT bindTo value
fun promptOf(value: String) = Label(value) styleAs LabelStyle.PROMPT

fun headingOf(value: String) = Label() styleAs LabelStyle.HEADING

operator fun Labeled.plusAssign(otherProperty: StringProperty) = run { textProperty() += otherProperty }

fun testIt(): Unit {
   val fred: StringProperty = SimpleStringProperty("abc")
   val label1 = Label() addStyle "label-prompt" bindTo fred
   val label2 = Label() styleAs LabelStyle.PROMPT bindTo fred
   val label3 = Label().styleAs(LabelStyle.PROMPT).bindTo(fred)
   val label4 = promptOf(fred)
}