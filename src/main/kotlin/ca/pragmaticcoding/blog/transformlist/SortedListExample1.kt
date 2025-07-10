package ca.pragmaticcoding.blog.transformlist

import ca.pragmaticcoding.widgetsfx.*
import javafx.application.Application
import javafx.beans.InvalidationListener
import javafx.beans.property.ReadOnlyIntegerWrapper
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.transformation.SortedList
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import kotlin.random.Random

class SortedListExample1 : Application() {

    private val obList: ObservableList<Int> = FXCollections.observableArrayList()
    private val sortedList = SortedList(obList, { a, b -> a.compareTo(b) })
    private val messages: StringProperty = SimpleStringProperty("")
    private val obTotalBinding = TotalBinding(obList)
    private val filterTotalBinding = TotalBinding(sortedList)
    private val fred = ReadOnlyIntegerWrapper(0)

    override fun start(stage: Stage) {
        populateList(obList)
        obList.subscribe { messages.value += "obList Invalidated \n" }
        sortedList.subscribe { messages.value += "sortedList Invalidated \n" }
        stage.scene = Scene(createContent()).addWidgetStyles()
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        center = TextArea().apply {
            textProperty().bind(messages)
        }
        left = HBox(
            10.0,
            VBox(
                6.0,
                HBox(
                    20.0, promptOf("obList"),
                    Label().bindTo(obTotalBinding.asString())
                ) alignTo Pos.CENTER_LEFT,
                ListView<Int>(obList).apply { maxWidth = 100.0 }),
            VBox(
                6.0,
                HBox(
                    10.0, promptOf("sortedList"),
                    Label().bindTo(filterTotalBinding.asString())
                ) alignTo Pos.CENTER_LEFT,
                ListView<Int>(sortedList).apply { maxWidth = 100.0 })
        )
        bottom = HBox(
            20.0,
            Button("Add 3").apply {
                setOnAction {
                    messages.value += "\n3 Button Clicked\n"
                    obList.add(3)
                }
            },
            Button("Add 15").apply {
                setOnAction {
                    messages.value += "\n15 Button Clicked\n"
                    obList.add(15)
                }
            },
            Button("Odds and Evens").apply {
                setOnAction {
                    messages.value += "\nComparator Button Clicked\n"
                    sortedList.comparator = Comparator { a, b ->
                        if ((a % 2) != 0) {
                            if ((b % 2) != 0) a.compareTo(b)
                            else -1
                        } else {
                            if ((b % 2) != 0) 1
                            else a.compareTo(b)
                        }
                    }
                }
            }
        ) padWith 10.0
        padding = Insets(20.0)
    }

    private fun populateList(list: MutableList<Int>) {
        repeat(10) {
            list += Random.nextInt(20)
        }
    }
}

class ImmutableStringValue(private val immutableValue: String?) : ObservableValue<String> {
    override fun addListener(listener: ChangeListener<in String>?) {}

    override fun removeListener(listener: ChangeListener<in String>?) {}

    override fun getValue(): String? = immutableValue

    override fun addListener(listener: InvalidationListener?) {}

    override fun removeListener(listener: InvalidationListener?) {}
}

fun main() = Application.launch(SortedListExample1::class.java)