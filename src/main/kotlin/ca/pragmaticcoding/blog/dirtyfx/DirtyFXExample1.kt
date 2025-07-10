package ca.pragmaticcoding.blog.dirtyfx


import ca.pragmaticcoding.widgetsfx.*
import javafx.application.Application
import javafx.beans.binding.BooleanExpression
import javafx.beans.binding.StringExpression
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.nield.dirtyfx.beans.DirtyStringProperty
import org.nield.dirtyfx.extensions.addTo
import org.nield.dirtyfx.tracking.CompositeDirtyProperty

class DirtyFXExample1 : Application() {

    private val model = PresentationModel()

    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 300.0, 300.0).addWidgetStyles()
        stage.title = "DirtyFX Example 1"
        stage.show()
    }

    private fun createContent(): Region = VBox(10.0).apply {
        children += HBox(10.0, Label("First Name: "), TextField().bindTo(model.firstName))
        children += HBox(10.0, Label("Last Name: "), TextField().bindTo(model.lastName))
        children += Label()
            .bindTo(
                StringExpression.stringExpression(
                    model
                        .dataChanged.map { if (it) "Changed" else "Not changed" })
            ) styleAs LabelStyle.PROMPT
        alignment = Pos.CENTER
    } padWith 20.0

}

fun main() {
    Application.launch(DirtyFXExample1::class.java)
}

private class PresentationModel() {
    private val _dataChanged = CompositeDirtyProperty()
    val dataChanged: ObservableBooleanValue
        get() = BooleanExpression.booleanExpression(_dataChanged)
    val firstName: StringProperty = DirtyStringProperty("").addTo(_dataChanged)
    val lastName: StringProperty = DirtyStringProperty("").addTo(_dataChanged)
}
