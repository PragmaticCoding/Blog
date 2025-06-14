package ca.pragmaticcoding.blog.observablelist

import javafx.application.Application
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.layout.*
import javafx.stage.Stage
import java.lang.ref.WeakReference
import kotlin.random.Random

class ObListExample3 : Application() {

    private val obList: ObservableList<Int> = FXCollections.observableArrayList()
    private val messages: StringProperty = SimpleStringProperty("")

    override fun start(stage: Stage) {
        obList.subscribe { messages.value += "Invalidated \n" }
        stage.scene = Scene(createContent()).apply { }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        val listView = ListView<Int>().apply {
            items = obList
        }
        left = listView
        right = FlowPane().apply { obList.bindToList(children) { createLabel(it) } }
        bottom = HBox(20.0).apply {
            children += Button("Add Item").apply {
                setOnAction { obList.add(Random.nextInt(100)) }
            }
            children += Button("Remove Item").apply {
                disableProperty().bind(listView.selectionModel.selectedItemProperty().isNull)
                setOnAction { obList.remove(listView.selectionModel.selectedItem) }
            }
        }
        padding = Insets(20.0)
    }

    private fun createLabel(labelInt: Int) = Label(labelInt.toString()).apply {
        minWidth = 80.0
        minHeight = 30.0
        border = Border(
            BorderStroke(
                javafx.scene.paint.Color.RED,
                BorderStrokeStyle.SOLID,
                null,
                BorderWidths(3.0)
            )
        )
        alignment = Pos.CENTER
    }
}

fun main() = Application.launch(ObListExample3::class.java)

fun <E, T> ObservableList<E>.bindToList(boundList: MutableList<T>, mapper: (E) -> T) = apply {
    val listRef: WeakReference<MutableList<T>?> = WeakReference(boundList)
    val listGone: BooleanProperty = SimpleBooleanProperty(false)
    val listener = ListChangeListener<E> { change ->
        listRef.get()?.let { otherList ->
            while (change.next()) {
                if (change.wasPermutated()) {
                    otherList.subList(change.from, change.to).clear()
                    otherList.addAll(
                        change.from,
                        change.list.subList(change.from, change.to).map(mapper) as Collection<T>
                    )
                } else {
                    if (change.wasRemoved()) {
                        otherList.subList(change.from, change.from + change.removedSize).clear()
                    }

                    if (change.wasAdded()) {
                        otherList.addAll(change.from, change.addedSubList.map(mapper))
                    }
                }
            }
        }
        listGone.value = (listRef.get() == null)
    }
    addListener(listener)
    listGone.subscribe { newVal -> if (newVal) removeListener(listener) }
}

