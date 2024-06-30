package ca.pragmaticcoding.examples.pseudo


import javafx.application.Application
import javafx.beans.property.ObjectPropertyBase
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.util.Builder

class Model {
    val enumProperty = SimpleObjectProperty<StatusPseudoClass>()
    val amountProperty = SimpleObjectProperty<Int>(-1)
}

class Interactor(private val model: Model) {
    init {
        model.enumProperty.bind(model.amountProperty.map { theValue ->
            when {
                theValue < 0 -> StatusPseudoClass.FAILED
                theValue == 0 -> StatusPseudoClass.NORMAL
                theValue < 3 -> StatusPseudoClass.WARNING
                else -> StatusPseudoClass.ERROR
            }
        })
    }
}

class Controller {
    private val model = Model()
    private val interactor = Interactor(model)
    private val viewBuilder = ViewBuilder(model)

    fun getView() = viewBuilder.build()
}

class ViewBuilder(private val model: Model) : Builder<Region> {
    private val statusProperty = PseudoClassSupplierProperty().apply {
        bind(model.enumProperty)
    }

    override fun build(): Region = VBox(10.0).apply {
        children += Label().apply {
            styleClass += "status-label"
            textProperty().bind(model.amountProperty.map { "This is a Label: $it" })
            statusProperty.setNode(this)
        }
        children += Button("Add 1").apply {
            setOnAction { model.amountProperty.value += 1 }
        }
        padding = Insets(50.0)
    }

}

class PseudoClassSupplierProperty : ObjectPropertyBase<PseudoClassSupplier?>() {
    private var node: Node? = null
    private var oldValue: PseudoClassSupplier? = null

    fun setNode(theNode: Node) {
        node = theNode
    }

    init {
        invalidated()
    }

    override fun invalidated() {
        node?.let { theNode ->
            oldValue?.let { theNode.pseudoClassStateChanged(it.getPseudoClass(), false) }
            value?.let { theNode.pseudoClassStateChanged(it.getPseudoClass(), true) }
        }
        oldValue = value
    }

    override fun getBean() = node
    override fun getName() = ""
}

class FrameworkApp : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(Controller().getView(), 380.0, 200.0).apply {
            EnumPseudoClassApp::class.java.getResource("test.css")?.toString()?.let { stylesheets += it }
        }
        stage.title = "PseudoClasses Wow!"
        stage.show()
    }
}

fun main() = Application.launch(FrameworkApp::class.java)


