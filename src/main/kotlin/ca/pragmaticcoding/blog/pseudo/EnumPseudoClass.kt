package ca.pragmaticcoding.examples.pseudo

import javafx.application.Application
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ObjectPropertyBase
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.css.PseudoClass
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage


interface PseudoClassSupplier {
    fun getPseudoClass(): PseudoClass
}

enum class StatusPseudoClass : PseudoClassSupplier {
    NORMAL, WARNING, ERROR, FAILED;

    companion object PseudoClasses {
        val normalPseudoClass: PseudoClass = PseudoClass.getPseudoClass("normal")
        val warningPseudoClass: PseudoClass = PseudoClass.getPseudoClass("warning")
        val errorPseudoClass: PseudoClass = PseudoClass.getPseudoClass("error")
        val failedPseudoClass: PseudoClass = PseudoClass.getPseudoClass("failed")
    }

    override fun getPseudoClass() = when (this) {
        NORMAL -> normalPseudoClass
        WARNING -> warningPseudoClass
        ERROR -> errorPseudoClass
        FAILED -> failedPseudoClass
    }
}


fun Node.bindPseudoClassEnum(property: ObservableValue<out PseudoClassSupplier>) {
    property.subscribe { oldValue, newValue ->
        oldValue?.let { pseudoClassStateChanged(it.getPseudoClass(), false) }
        newValue?.let { pseudoClassStateChanged(it.getPseudoClass(), true) }
    }
    property.value?.getPseudoClass()?.let { pseudoClassStateChanged(it, true) }
}

class IntRangePseudoProperty(private val node: Node) : ObjectPropertyBase<Int>() {

    private var oldValue: Int? = null

    init {
        invalidated()
    }

    override fun invalidated() {
        oldValue?.let { node.pseudoClassStateChanged(getPseudoClassFromValue(it), false) }
        value?.let { node.pseudoClassStateChanged(getPseudoClassFromValue(it), true) }
        oldValue = value
    }

    private fun getPseudoClassFromValue(theValue: Int): PseudoClass = when {
        theValue < 0 -> failedPseudoClass
        theValue == 0 -> normalPseudoClass
        theValue < 3 -> warningPseudoClass
        else -> errorPseudoClass
    }

    companion object PseudoClasses {
        val normalPseudoClass: PseudoClass = PseudoClass.getPseudoClass("normal")
        val warningPseudoClass: PseudoClass = PseudoClass.getPseudoClass("warning")
        val errorPseudoClass: PseudoClass = PseudoClass.getPseudoClass("error")
        val failedPseudoClass: PseudoClass = PseudoClass.getPseudoClass("failed")
    }

    override fun getBean(): Any = node

    override fun getName(): String = "Integer Range"
}

class IntPseudoProperty(private val node: Node, private val translator: (Int) -> PseudoClass) :
    ObjectPropertyBase<Int>() {
    private var oldValue: Int? = null

    init {
        invalidated()
    }

    override fun invalidated() {
        oldValue?.let { node.pseudoClassStateChanged(translator(it), false) }
        value?.let { node.pseudoClassStateChanged(translator(it), true) }
        oldValue = value
    }

    override fun getBean(): Any = node

    override fun getName(): String = "Integer Range"
}

class IntRangeTranslator : (Int) -> PseudoClass {

    override fun invoke(theValue: Int): PseudoClass = when {
        theValue < 0 -> failedPseudoClass
        theValue == 0 -> normalPseudoClass
        theValue < 3 -> warningPseudoClass
        else -> errorPseudoClass
    }

    companion object PseudoClasses {
        val normalPseudoClass: PseudoClass = PseudoClass.getPseudoClass("normal")
        val warningPseudoClass: PseudoClass = PseudoClass.getPseudoClass("warning")
        val errorPseudoClass: PseudoClass = PseudoClass.getPseudoClass("error")
        val failedPseudoClass: PseudoClass = PseudoClass.getPseudoClass("failed")
    }
}

class PseudoProperty<T>(initialValue: T, private val translator: (T) -> PseudoClass) : ObjectPropertyBase<T>() {
    private var oldValue: T? = null
    private var node: Node? = null

    fun setNode(newNode: Node) {
        node = newNode
    }

    init {
        value = initialValue
        invalidated()
    }

    override fun invalidated() {
        println("Invalidated $value")
        oldValue?.let { node?.pseudoClassStateChanged(translator(it), false) }
        value?.let { node?.pseudoClassStateChanged(translator(it), true) }
        oldValue = value
    }

    override fun getBean(): Any = node ?: Unit
    override fun getName(): String = "PseudoClass Property"
}

class SimplePseudoProperty<T>(initialValue: T) : ObjectPropertyBase<T>() {

    private var translator: ((T) -> PseudoClass)? = null
    private var oldValue: T? = null
    private var node: Node? = null

    infix fun setNode(newNode: Node) = apply {
        node = newNode
    }

    infix fun setTranslator(newTranslator: ((T) -> PseudoClass)?) = apply {
        translator = newTranslator
    }

    init {
        value = initialValue
        invalidated()
    }

    override fun invalidated() {
        println("Invalidated $value")
        translator?.let { translator ->
            oldValue?.let { node?.pseudoClassStateChanged(translator(it), false) }
            value?.let { node?.pseudoClassStateChanged(translator(it), true) }
        }
        oldValue = value
    }

    override fun getBean(): Any = node ?: Unit
    override fun getName(): String = "PseudoClass Property"
}

class EnumPseudoClassApp : Application() {
    private val amount = SimplePseudoProperty(-1) setTranslator { theValue ->
        when {
            theValue < 0 -> failedPseudoClass
            theValue == 0 -> normalPseudoClass
            theValue < 3 -> warningPseudoClass
            else -> errorPseudoClass
        }
    }

    companion object PseudoClasses {
        val normalPseudoClass: PseudoClass = PseudoClass.getPseudoClass("normal")
        val warningPseudoClass: PseudoClass = PseudoClass.getPseudoClass("warning")
        val errorPseudoClass: PseudoClass = PseudoClass.getPseudoClass("error")
        val failedPseudoClass: PseudoClass = PseudoClass.getPseudoClass("failed")
    }

    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 380.0, 200.0).apply {
            EnumPseudoClassApp::class.java.getResource("test.css")?.toString()?.let { stylesheets += it }
        }
        stage.title = "PseudoClasses Wow!"
        stage.show()
    }

    private fun createContent(): Region = VBox(10.0).apply {
        children += Label().apply {
            styleClass += "status-label"
            textProperty().bind(amount.map { "This is a Label: $it" })
            amount.setNode(this)
        }
        children += Button("Add 1").apply {
            setOnAction { amount.value += 1 }
        }
        padding = Insets(50.0)
    }
}

class EnumPseudoClassApp1 : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 380.0, 200.0).apply {
            EnumPseudoClassApp::class.java.getResource("test.css")?.toString()?.let { stylesheets += it }
        }
        stage.title = "PseudoClasses Wow!"
        stage.show()
    }

    private fun createContent(): Region = VBox(10.0).apply {
        val status: ObjectProperty<StatusPseudoClass> = SimpleObjectProperty()
        children += Label("This is a Label").apply {
            styleClass += "status-label"
            bindPseudoClassEnum(status)
        }
        val toggleGroup = ToggleGroup()
        children += HBox(10.0).apply {
            children += ToggleButton("Normal").apply {
                setOnAction { status.value = StatusPseudoClass.NORMAL }
                toggleGroup.toggles += this
            }
            children += ToggleButton("Warning").apply {
                setOnAction { status.value = StatusPseudoClass.WARNING }
                toggleGroup.toggles += this
            }
            children += ToggleButton("Error").apply {
                setOnAction { status.value = StatusPseudoClass.ERROR }
                toggleGroup.toggles += this
            }
            children += ToggleButton("Failed").apply {
                setOnAction { status.value = StatusPseudoClass.FAILED }
                toggleGroup.toggles += this
            }
        }
        padding = Insets(50.0)
    }
}

fun main() = Application.launch(EnumPseudoClassApp1::class.java)


