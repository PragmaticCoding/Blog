package ca.pragmaticcoding.blog.tablecolumns


import javafx.application.Application
import javafx.beans.binding.ObjectBinding
import javafx.beans.property.*
import javafx.css.*
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Menu
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Region
import javafx.stage.Stage
import kotlin.math.roundToInt
import kotlin.random.Random

class Example1 : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 400.0, 350.0).apply {
            Example1::class.java.getResource("Example0.css")?.toString()?.let { stylesheets += it }
        }
        Menu()
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        center = TableView<TableData1>().apply {
            columns += TableColumn<TableData1, String>("Column A").apply {
                setCellValueFactory { it.value.value1 }
                styleClass += "a-column"
            }
            columns += TemperatureColumn<TableData1> { it.value4 } outputAs TemperatureUnit.F
            columns += TemperatureColumn<TableData1> { it.value4 }
            columns += TableColumn<TableData1, String>("Column C").apply {
                setCellValueFactory { it.value.value2 }
                styleClass += "c-column"
            }
            for (x in 1..30) items += TableData1(
                "A",
                "B",
                "C",
                Random.nextDouble(-30.0, 55.0)
            )
        }
        padding = Insets(10.0)

    }
}

class TemperatureColumn<T>(extractor: (data: T) -> DoubleProperty) : TableColumn<T, Double>() {

    private val outputUnits: StyleableObjectProperty<TemperatureUnit> =
        SimpleStyleableObjectProperty(OUTPUT_UNITS_META_DATA, TemperatureUnit.C)

    private val nunDecimals: StyleableObjectProperty<Number> =
        SimpleStyleableObjectProperty<Number>(NUM_DECIMALS_META_DATA, 1)

    infix fun outputAs(newOutput: TemperatureUnit) = this.apply { outputUnits.value = newOutput }

    companion object StylingStuff {
        val freezingPseudoClass: PseudoClass = PseudoClass.getPseudoClass("freezing")
        val OUTPUT_UNITS_META_DATA: CssMetaData<TemperatureTableCell<*>, TemperatureUnit> = object :
            CssMetaData<TemperatureTableCell<*>, TemperatureUnit>(
                "-wfx-temperature-unit",
                StyleConverter.getEnumConverter(TemperatureUnit::class.java)
            ) {
            override fun isSettable(styleable: TemperatureTableCell<*>) =
                !(styleable.outputUnits.isBound)

            override fun getStyleableProperty(styleable: TemperatureTableCell<*>) = styleable.outputUnits
        }
        val NUM_DECIMALS_META_DATA: CssMetaData<TemperatureTableCell<*>, Number> = object :
            CssMetaData<TemperatureTableCell<*>, Number>(
                "-wfx-num-decimal",
                StyleConverter.getSizeConverter()
            ) {
            override fun isSettable(styleable: TemperatureTableCell<*>) =
                !(styleable.numDecimals.isBound)

            override fun getStyleableProperty(styleable: TemperatureTableCell<*>) = styleable.numDecimals
        }

        private val cssMetaDataList =
            (TableColumn.getClassCssMetaData() + OUTPUT_UNITS_META_DATA) as MutableList

        fun getClassCssMetaData() = cssMetaDataList
    }

    init {
        styleClass += "temperature-column"
        setCellValueFactory {
            extractor.invoke(it.value).asObject()
        }
        TemperatureColumn::class.java.getResource("thermometer24.png")?.toString()?.let { graphic = ImageView(it) }
        setCellFactory { TemperatureTableCell(outputUnits, nunDecimals) }
    }

}

class TemperatureTableCell<T>(
    val outputUnits: StyleableObjectProperty<TemperatureUnit>,
    val numDecimals: StyleableObjectProperty<Number>
) :
    TableCell<T, Double>() {

    override fun getControlCssMetaData() =
        (super.getControlCssMetaData() + TemperatureColumn.getClassCssMetaData()) as MutableList

    init {
        itemProperty().subscribe { newValue ->
            pseudoClassStateChanged(TemperatureColumn.freezingPseudoClass, (newValue?.let { (it <= 0.0) } ?: false))
        }
        textProperty().bind(
            ConversionBinding(itemProperty(), outputUnits)
                .map { rawValue -> rawValue?.let { (rawValue * 10.0).roundToInt() / 10.0 }?.toString() })
    }
}


enum class TemperatureUnit { C, F, K }


class ConversionBinding(
    private val celciusValue: ObjectProperty<Double>,
    private val outputUnits: ObjectProperty<TemperatureUnit>
) : ObjectBinding<Double?>() {
    init {
        super.bind(celciusValue, outputUnits)
    }

    override fun computeValue(): Double? = when (outputUnits.value) {
        TemperatureUnit.K -> celciusValue.value?.let { it + 270.0 }
        TemperatureUnit.F -> celciusValue.value?.let { (it * 9 / 5) + 32 }
        else -> celciusValue.value
    }
}


class TableData1(initVal1: String, initVal2: String, initVal3: String, initVal4: Double) {
    val value1: StringProperty = SimpleStringProperty(initVal1)
    val value2: StringProperty = SimpleStringProperty(initVal2)
    val value3: StringProperty = SimpleStringProperty(initVal3)
    val value4: DoubleProperty = SimpleDoubleProperty(initVal4)
}

fun main() = Application.launch(Example1::class.java)