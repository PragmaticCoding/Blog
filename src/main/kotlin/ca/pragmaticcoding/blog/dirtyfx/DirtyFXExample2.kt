package ca.pragmaticcoding.blog.dirtyfx


import ca.pragmaticcoding.widgetsfx.*
import javafx.application.Application
import javafx.beans.binding.BooleanExpression
import javafx.beans.binding.StringExpression
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.nield.dirtyfx.beans.DirtyStringProperty
import org.nield.dirtyfx.extensions.addTo
import org.nield.dirtyfx.tracking.CompositeDirtyProperty

class DirtyFXExample2 : Application() {

    private val model = PresentationModel1()

    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 600.0, 300.0).addWidgetStyles()
        stage.title = "DirtyFX Example 2"
        stage.show()
    }

    private fun createContent(): Region = HBox(20.0).apply {
        children += VBox(10.0).apply {
            children += HBox(10.0, Label("First Name: "), TextField().bindTo(model.firstName))
            children += HBox(10.0, Label("Last Name: "), TextField().bindTo(model.lastName))
            children += Label()
                .bindTo(
                    StringExpression.stringExpression(
                        model
                            .dataChanged.map { if (it) "Changed" else "Not changed" })
                ) styleAs LabelStyle.PROMPT
            children += Button("Save Data").apply {
                setOnAction { evt -> model.trackSavedData() }
                disableProperty().bind(BooleanExpression.booleanExpression(model.dataChanged).not())
            }
            alignment = Pos.CENTER
        }
        children += ListView<String>().apply {
            items = model.savedNames
        }

    } padWith 20.0

}

fun main() {
    Application.launch(DirtyFXExample2::class.java)
}

private class PresentationModel1() {
    private val _dataChanged = CompositeDirtyProperty()
    val dataChanged: ObservableBooleanValue
        get() = BooleanExpression.booleanExpression(_dataChanged)
    private val _firstName = DirtyStringProperty("").addTo(_dataChanged)
    val firstName: StringProperty
        get() = _firstName
    private val _lastName = DirtyStringProperty("").addTo(_dataChanged)
    val lastName: StringProperty
        get() = _lastName
    val savedNames: ObservableList<String> = FXCollections.observableArrayList()

    fun trackSavedData() {
        savedNames += firstName.value + " " + lastName.value
        _firstName.rebaseline()
        _lastName.rebaseline()
    }
}
