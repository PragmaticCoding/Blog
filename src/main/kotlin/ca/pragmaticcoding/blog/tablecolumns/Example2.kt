package ca.pragmaticcoding.blog.tablecolumns


import javafx.application.Application
import javafx.beans.property.DoubleProperty
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

class Example2 : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 400.0, 350.0).apply {
            Example2::class.java.getResource("Example0.css")?.toString()?.let { stylesheets += it }
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
            columns += TemperatureColumn2<TableData1> { it.value4 } outputAs TemperatureUnit.F
            columns += TemperatureColumn2<TableData1> { it.value4 }
            columns += TableColumn<TableData1, String>("Column C").apply {
                setCellValueFactory { it.value.value3 }
                styleClass += "c-column"
            }
            repeat(30) {
                items += TableData1(
                    "A",
                    "B",
                    "C",
                    Random.nextDouble(-30.0, 55.0)
                )
            }

        }
        padding = Insets(10.0)

    }
}

class TemperatureColumn2<T>(extractor: (data: T) -> DoubleProperty) : TableColumn<T, Double>() {

    private val outputUnits: StyleableObjectProperty<TemperatureUnit> =
        SimpleStyleableObjectProperty(OUTPUT_UNITS_META_DATA, TemperatureUnit.C)
    private val numDecimals: StyleableObjectProperty<Number> =
        SimpleStyleableObjectProperty<Number>(NUM_DECIMALS_META_DATA, 1)
    private val coldPoint: StyleableObjectProperty<Number> =
        SimpleStyleableObjectProperty<Number>(COLD_POINT_META_DATA, 1000)

    infix fun outputAs(newOutput: TemperatureUnit) = this.apply { outputUnits.value = newOutput }

    companion object StylingStuff {
        val freezingPseudoClass: PseudoClass = PseudoClass.getPseudoClass("freezing")
        val coldPseudoClass: PseudoClass = PseudoClass.getPseudoClass("cold")
        val OUTPUT_UNITS_META_DATA: CssMetaData<TemperatureTableCell2<*>, TemperatureUnit> = object :
            CssMetaData<TemperatureTableCell2<*>, TemperatureUnit>(
                "-wfx-temperature-unit",
                StyleConverter.getEnumConverter(TemperatureUnit::class.java)
            ) {
            override fun isSettable(styleable: TemperatureTableCell2<*>) = !(styleable.outputUnits.isBound)

            override fun getStyleableProperty(styleable: TemperatureTableCell2<*>) = styleable.outputUnits
        }
        val NUM_DECIMALS_META_DATA: CssMetaData<TemperatureTableCell2<*>, Number> = object :
            CssMetaData<TemperatureTableCell2<*>, Number>(
                "-wfx-num-decimal",
                StyleConverter.getSizeConverter()
            ) {
            override fun isSettable(styleable: TemperatureTableCell2<*>) =
                !(styleable.numDecimals.isBound)

            override fun getStyleableProperty(styleable: TemperatureTableCell2<*>) = styleable.numDecimals
        }
        val COLD_POINT_META_DATA: CssMetaData<TemperatureTableCell2<*>, Number> = object :
            CssMetaData<TemperatureTableCell2<*>, Number>(
                "-wfx-cold-point",
                StyleConverter.getSizeConverter()
            ) {
            override fun isSettable(styleable: TemperatureTableCell2<*>) =
                !(styleable.coldPoint.isBound)

            override fun getStyleableProperty(styleable: TemperatureTableCell2<*>) = styleable.coldPoint
        }

        private val cssMetaDataList =
            (TableColumn.getClassCssMetaData() +
                    OUTPUT_UNITS_META_DATA +
                    NUM_DECIMALS_META_DATA +
                    COLD_POINT_META_DATA) as MutableList

        fun getClassCssMetaData() = cssMetaDataList
    }

    init {
        styleClass += "temperature-column"
        setCellValueFactory {
            extractor.invoke(it.value).asObject()
        }
        TemperatureColumn2::class.java.getResource("thermometer24.png")?.toString()?.let { graphic = ImageView(it) }
        setCellFactory { TemperatureTableCell2(outputUnits, numDecimals, coldPoint) }
    }

}

class TemperatureTableCell2<T>(
    val outputUnits: StyleableObjectProperty<TemperatureUnit>,
    val numDecimals: StyleableObjectProperty<Number>,
    val coldPoint: StyleableObjectProperty<Number>,
) :
    TableCell<T, Double>() {

    override fun getControlCssMetaData() =
        (super.getControlCssMetaData() + TemperatureColumn2.getClassCssMetaData()) as MutableList

    init {
        itemProperty().subscribe { newValue ->
            pseudoClassStateChanged(TemperatureColumn2.freezingPseudoClass, (newValue?.let { (it <= 0.0) } ?: false))
            pseudoClassStateChanged(
                TemperatureColumn2.coldPseudoClass,
                (newValue?.let { (it <= coldPoint.value.toDouble()) } ?: false))
        }
        textProperty().bind(
            ConversionBinding(itemProperty(), outputUnits)
                .map { rawValue -> rawValue?.let { (rawValue * 10.0).roundToInt() / 10.0 }?.toString() })
    }
}


fun main() = Application.launch(Example2::class.java)