package ca.pragmaticcoding.blog.transformlist

import ca.pragmaticcoding.widgetsfx.*
import javafx.application.Application
import javafx.beans.binding.IntegerBinding
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
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
import java.util.function.Predicate

class FilterListExample1 : Application() {

    private val obList: ObservableList<Int> = FXCollections.observableArrayList()
    private val filterList = FilteredList(obList) { item -> item > 6 }
    private val messages: StringProperty = SimpleStringProperty("")
    private val obTotalBinding = TotalBinding(obList)
    private val filterTotalBinding = TotalBinding(filterList)

    override fun start(stage: Stage) {
        populateList(obList)
        obList.subscribe { messages.value += "obList Invalidated \n" }
        filterList.subscribe { messages.value += "filterList Invalidated \n" }
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
                    10.0, promptOf("filterList"),
                    Label().bindTo(filterTotalBinding.asString())
                ) alignTo Pos.CENTER_LEFT,
                ListView<Int>(filterList).apply { maxWidth = 100.0 })
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
            Button("< 10").apply {
                setOnAction {
                    messages.value += "\nPredicate Button Clicked\n"
                    filterList.predicate = Predicate { item -> item < 10 }
                }
            }
        ) padWith 10.0
        padding = Insets(20.0)
    }

    private fun populateList(list: MutableList<Int>) {
        for (x in 0..10) {
            list += x
        }
    }
}

class TotalBinding(private val list: ObservableList<Int>) : IntegerBinding() {
    init {
        super.bind(list)
    }

    override fun computeValue(): Int = list.sum()
}

fun main() = Application.launch(FilterListExample1::class.java)