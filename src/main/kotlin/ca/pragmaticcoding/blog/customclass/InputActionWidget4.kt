package ca.pragmaticcoding.blog.customclass

import javafx.animation.PauseTransition
import javafx.application.Application
import javafx.beans.InvalidationListener
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.css.*
import javafx.css.converter.EnumConverter
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.util.Duration

class InputActionWidget4(
    private val boundProperty: StringProperty,
    labelText: String = "Prompt:",
    buttonText: String = "Start"
) : Region() {

    private var buttonAction: ButtonRunner = {}
    private val actionRunning: BooleanProperty = SimpleBooleanProperty(false)
    private val label = Label(labelText)
    private val textField = TextField()
    private val button = Button(buttonText)

    fun labelTextProperty() = label.textProperty()
    var labelText: String
        get() = label.text
        set(value) {
            label.text = value
        }

    fun buttonTextProperty() = button.textProperty()
    var buttonText: String
        get() = button.text
        set(value) {
            button.text = value
        }

    fun textFieldTextProperty() = textField.textProperty()
    var textFieldText: String
        get() = textField.text
        set(value) {
            textField.text = value
        }

    fun textFormatterProperty() = textField.textFormatterProperty()
    var textFormatter: TextFormatter<*>?
        get() = textField.textFormatter
        set(value) {
            textField.textFormatter = value
        }

    fun tagGapProperty() = tagGapImpl
    private val tagGapImpl: StyleableDoubleProperty = SimpleStyleableDoubleProperty(GAP_META_DATA, this, "tagGap")
    var tagGap: Double
        get() = tagGapImpl.value
        set(value) = tagGapImpl.set(value)

    fun orientationPropery() = orientationImpl
    private val orientationImpl: StyleableObjectProperty<Orientation> =
        SimpleStyleableObjectProperty(ORIENTATION_META_DATA, this, "orientation", Orientation.HORIZONTAL)
    var orientation: Orientation
        get() = orientationImpl.get()
        set(value) = orientationImpl.set(value)

    companion object CssStuff {
        val TEXTFIELD_FOCUSED: PseudoClass = PseudoClass.getPseudoClass("textfield-focused");
        val GAP_META_DATA: CssMetaData<InputActionWidget4, Number> =
            object : CssMetaData<InputActionWidget4, Number>("-wfx-gap", StyleConverter.getSizeConverter(), 6.0) {
                override fun isSettable(styleable: InputActionWidget4) = !styleable.tagGapImpl.isBound
                override fun getStyleableProperty(styleable: InputActionWidget4) = styleable.tagGapImpl
            }
        val ORIENTATION_META_DATA: CssMetaData<InputActionWidget4, Orientation> = object :
            CssMetaData<InputActionWidget4, Orientation>("-wfx-orientation", EnumConverter(Orientation::class.java)) {
            override fun isSettable(styleable: InputActionWidget4) = !styleable.orientationImpl.isBound
            override fun getStyleableProperty(styleable: InputActionWidget4) = styleable.orientationImpl
        }
        private val cssMetaDataList =
            (Region.getClassCssMetaData() + GAP_META_DATA + ORIENTATION_META_DATA) as MutableList

        fun getClassCssMetaData() = cssMetaDataList
    }

    override fun getCssMetaData() = getClassCssMetaData()

    init {
        styleClass += "input-action-widget"
        configureComponents()
        createLayout()
        orientationImpl.addListener(InvalidationListener { createLayout() })
    }

    private fun configureComponents() {
        textFieldTextProperty().bindBidirectional(boundProperty)
        configureButton()
        textField.focusedProperty().addListener { _, _, newValue ->
            button.pseudoClassStateChanged(TEXTFIELD_FOCUSED, newValue);
        }
    }

    private fun createLayout() {
        children.clear()
        children += if (orientationImpl.value == Orientation.HORIZONTAL) createHBox() else createVBox()
    }

    private fun createHBox() = HBox(4.0).apply {
        children += listOf(label, HBox(0.0, textField, button))
        spacingProperty().bind(tagGapImpl)
        alignment = Pos.CENTER_LEFT
        minWidth = 200.0
    }

    private fun createVBox() = VBox(4.0).apply {
        children += listOf(label, HBox(0.0, textField, button))
        spacingProperty().bind(tagGapImpl)
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

class InputActionWidgetStyle4 : Application() {

    private val name: StringProperty = SimpleStringProperty("")
    private val results: StringProperty = SimpleStringProperty("No search done")

    override fun start(stage: Stage) {
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA)
//        InputActionWidgetStyle4::class.java
//            .getResource("/css/default.css")
//            ?.toString()
//            ?.let { StyleManager.getInstance().addUserAgentStylesheet(it) }
        stage.scene = Scene(createContent())
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        center = InputActionWidget4(name).setButtonAction { postRunAction ->
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

fun main() = Application.launch(InputActionWidgetStyle4::class.java)