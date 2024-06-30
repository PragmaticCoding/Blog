package ca.pragmaticcoding.blog.actionproperty

import ca.pragmaticcoding.widgetsfx.padWith
import javafx.animation.FadeTransition
import javafx.animation.Interpolator
import javafx.application.Application
import javafx.beans.property.BooleanProperty
import javafx.beans.property.BooleanPropertyBase
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ToggleButton
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.util.Builder
import javafx.util.Duration

class FadeActionProperty(private val node: Node, private val duration: Duration) : BooleanPropertyBase() {
    override fun getBean() = node
    override fun getName() = "Fade Action Property"

    private val transition = FadeTransition(duration, node)

    override fun invalidated() {
        if (value) node.isVisible = true
        transition.interpolator = Interpolator.EASE_IN
        transition.toValue = if (value) 1.0 else 0.0
        transition.setOnFinished { node.isVisible = value }
        transition.playFromStart()
    }
}

infix fun Node.addFade(duration: Duration): FadeActionProperty = FadeActionProperty(this, duration)

fun <T : Node> T.bindFade(duration: Duration, boundTo: ObservableBooleanValue): T =
    apply { addFade(duration).bind(boundTo) }

class ActionPropertyViewBuilder1 : Builder<Region> {
    private val nodeVisible: BooleanProperty = SimpleBooleanProperty(true)

    override fun build(): Region = VBox(10.0).apply {
        children += VBox(
            10.0,
            Label("Line 1"),
            Label("Line 2").bindFade(Duration.millis(700.0), nodeVisible),
            Label("Line 3"),
        )
        children += ToggleButton("Visible").apply {
            nodeVisible.bind(selectedProperty())
            isSelected = true
        }
        minWidth = 300.0
    } padWith 20.0
}

class ActionPropertyApplication1 : Application() {
    override fun start(stage: Stage) {
        with(stage) {
            scene = Scene(ActionPropertyViewBuilder1().build())
            show()
        }
    }
}


fun main() {
    Application.launch(ActionPropertyApplication1::class.java)
}