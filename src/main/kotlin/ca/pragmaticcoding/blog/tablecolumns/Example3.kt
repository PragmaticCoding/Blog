package ca.pragmaticcoding.blog.tablecolumns


import javafx.application.Application
import javafx.beans.property.BooleanProperty
import javafx.beans.property.BooleanProperty.booleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.css.*
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.stage.Stage
import kotlin.random.Random

class Example3 : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 400.0, 350.0).apply {
            Example3::class.java.getResource("Example0.css")?.toString()?.let { stylesheets += it }
        }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        center = TableView<TableData3>().apply {
            columns += TableColumn<TableData3, String>("Column A").apply {
                setCellValueFactory { it.value.value1 }
                styleClass += "a-column"
            }
            columns += BooleanColumn<TableData3>("Column C") { it.value4 }.apply {
                styleClass += "c-column"
            } withFalseValues false withCircles true withCircleSize 18.0
            repeat(30) {
                items += TableData3(
                    "A",
                    "B",
                    "C",
                    Random.nextBoolean()
                )
            }

        }
        padding = Insets(10.0)

    }
}

/**
 * TableColumn for the display of Boolean values.
 *
 * ## Infix Decorator Methods
 * Values can be displayed as either ImageViewss or Circles, with ImageViews as the default behaviour.  Use
 * `BooleanColumn.withCircles(true)` to change to Circles.
 *
 * Graphics may be shown for both true and false values, or for only true values.  The default is to
 * display graphics for both.  Use `BooleanColumn.withFalseValues(false)` to suppress graphics for
 * false values.
 *
 * If Circles are used, the radius of the Circles may be specified with through `BooleanColurm.withCircleSize(Double)`.
 * The default Circle radius is 7.0px.
 *
 * ## StyleSheet Selectors
 * - Column: `boolean-column`
 * - TableCell: `boolean-table-cell`
 * - ImageViews: `true-image` and `false-image`
 * - Circles: `true-circle` and `false-circle`
 *
 * ## Styleable Properties
 * - Show false values: `-wfx-show-false`
 * - Use Circles: `-wfx-use-circles`
 * - Circle size: `-wfx-circle-size`
 *
 * @param title Column heading title
 * @param extractor Function that will return a Boolean property give an item of the TableView type.
 */
class BooleanColumn<T>(title: String, extractor: (data: T) -> BooleanProperty) : TableColumn<T, Boolean>(title) {

    private val showFalse: StyleableBooleanProperty = SimpleStyleableBooleanProperty(SHOW_FALSE_META_DATA, true)
    private val useCircles: StyleableBooleanProperty = SimpleStyleableBooleanProperty(USE_CIRCLES_META_DATA, false)
    private val circleSize: StyleableDoubleProperty = SimpleStyleableDoubleProperty(CIRCLE_SIZE_META_DATA, 7.0)

    infix fun withCircles(withCircles: Boolean) = apply {
        useCircles.value = withCircles
    }

    infix fun withCircleSize(circleRadius: Double) = apply {
        circleSize.value = circleRadius
    }

    infix fun withFalseValues(falseValues: Boolean) = apply {
        showFalse.value = falseValues
    }

    companion object StylingStuff {
        val SHOW_FALSE_META_DATA: CssMetaData<BooleanTableCell<*>, Boolean> = object :
            CssMetaData<BooleanTableCell<*>, Boolean>(
                "-wfx-show-false",
                StyleConverter.getBooleanConverter()
            ) {
            override fun isSettable(styleable: BooleanTableCell<*>) = !(styleable.showFalse.isBound)

            override fun getStyleableProperty(styleable: BooleanTableCell<*>) = styleable.showFalse
        }
        val USE_CIRCLES_META_DATA: CssMetaData<BooleanTableCell<*>, Boolean> = object :
            CssMetaData<BooleanTableCell<*>, Boolean>(
                "-wfx-use-circles",
                StyleConverter.getBooleanConverter()
            ) {
            override fun isSettable(styleable: BooleanTableCell<*>) = !(styleable.useCircles.isBound)

            override fun getStyleableProperty(styleable: BooleanTableCell<*>) = styleable.useCircles
        }
        val CIRCLE_SIZE_META_DATA: CssMetaData<BooleanTableCell<*>, Number> = object :
            CssMetaData<BooleanTableCell<*>, Number>(
                "-wfx-circle-size",
                StyleConverter.getSizeConverter()
            ) {
            override fun isSettable(styleable: BooleanTableCell<*>) = !(styleable.circleSize.isBound)

            override fun getStyleableProperty(styleable: BooleanTableCell<*>) = styleable.circleSize
        }

        private val cssMetaDataList =
            (TableColumn.getClassCssMetaData() + SHOW_FALSE_META_DATA + USE_CIRCLES_META_DATA + CIRCLE_SIZE_META_DATA) as MutableList

        fun getClassCssMetaData() = cssMetaDataList
    }

    init {
        styleClass += "boolean-column"
        setCellValueFactory {
            extractor.invoke(it.value).asObject()
        }
        setCellFactory { BooleanTableCell(showFalse, useCircles, circleSize) }
    }
}

class BooleanTableCell<T>(
    val showFalse: StyleableBooleanProperty,
    val useCircles: StyleableBooleanProperty,
    val circleSize: StyleableDoubleProperty
) : TableCell<T, Boolean>() {

    override fun getControlCssMetaData() =
        (super.getControlCssMetaData() + BooleanColumn.getClassCssMetaData()) as MutableList

    init {
        val booleanItem = booleanProperty(itemProperty())
        styleClass += "boolean-table-cell"
        graphic = StackPane(
            ImageView(BooleanColumn::class.java.getResource("checkmark.png")?.toString()).apply {
                fitWidth = 14.0
                fitHeight = 14.0
                visibleProperty().bind(booleanItem.and(useCircles.not()))
                this.styleClass += "true-image"
            },
            ImageView(BooleanColumn::class.java.getResource("crossmark.png")?.toString()).apply {
                fitWidth = 14.0
                fitHeight = 14.0
                visibleProperty().bind(booleanItem.not().and(showFalse).and(useCircles.not()))
                this.styleClass += "false-image"
            },
            Circle().apply {
                visibleProperty().bind(booleanItem.and(useCircles))
                radiusProperty().bind(circleSize)
                fill = Color.MEDIUMSEAGREEN
                styleClass += "true-circle"
            },
            Circle().apply {
                visibleProperty().bind(booleanItem.not().and(useCircles).and(showFalse))
                radiusProperty().bind(circleSize)
                fill = Color.FIREBRICK
                styleClass += "false-circle"
            }
        )
    }
}

class TableData3(initVal1: String, initVal2: String, initVal3: String, initVal4: Boolean) {
    val value1: StringProperty = SimpleStringProperty(initVal1)
    val value2: StringProperty = SimpleStringProperty(initVal2)
    val value3: StringProperty = SimpleStringProperty(initVal3)
    val value4: BooleanProperty = SimpleBooleanProperty(initVal4)
}

fun main() = Application.launch(Example3::class.java)