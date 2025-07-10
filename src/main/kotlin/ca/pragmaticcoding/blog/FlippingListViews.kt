package ca.pragmaticcoding.blog

import javafx.application.Application
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Scene
import javafx.scene.control.ListView
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.util.Builder

class FlippingListViewsModel() {
    val list1: ObservableList<String> = FXCollections.observableArrayList()
    val list2: ObservableList<String> = FXCollections.observableArrayList()
    val list3: ObservableList<String> = FXCollections.observableArrayList()
}

class FlippingListViewInteractor(private val model: FlippingListViewsModel) {
    init {
        model.list1.addAll("Red", "Blue", "Green")
        model.list2.addAll("Dog", "Cat", "Zebra")
        model.list3.addAll("Knife", "Fork", "Spoon")
    }
}

class FlippingListViewsViewBuilder(private val model: FlippingListViewsModel) : Builder<Region> {
    private val showList1: BooleanProperty = SimpleBooleanProperty(false)
    private val showList2: BooleanProperty = SimpleBooleanProperty(false)
    private val showList3: BooleanProperty = SimpleBooleanProperty(false)

    override fun build(): Region = VBox(10.0).apply {
        children += StackPane(
            addListView(showList1, model.list1),
            addListView(showList2, model.list2),
            addListView(showList3, model.list3)
        )
        children += HBox(12.0).apply {
            val toggleGroup = ToggleGroup()
            children += addButton("List 1", showList1, toggleGroup, true)
            children += addButton("List 2", showList2, toggleGroup, false)
            children += addButton("List 3", showList3, toggleGroup, false)
        }
    }

    private fun addButton(text: String, boundTo: BooleanProperty, tg: ToggleGroup, selected: Boolean) =
        ToggleButton(text).apply {
            boundTo.bind(selectedProperty())
            isSelected = selected
            tg.toggles += this
        }

    private fun addListView(showList: ObservableBooleanValue, list: ObservableList<String>) = ListView<String>().apply {
        items = list
        visibleProperty().bind(showList)
        managedProperty().bind(showList)
    }
}

class FlippingListViewsController() {
    private val model = FlippingListViewsModel()
    private val interactor = FlippingListViewInteractor(model)
    private val viewBuilder = FlippingListViewsViewBuilder(model)
    fun getView() = viewBuilder.build()
}

class FlippingListViewsApplication : Application() {
    override fun start(stage: Stage) {
        with(stage) {
            scene = Scene(FlippingListViewsController().getView())
            show()
        }
    }
}

fun main() {
    Application.launch(FlippingListViewsApplication::class.java)
}