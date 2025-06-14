package ca.pragmaticcoding.blog.mvciquick

import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.util.Builder

class ViewBuilder(private val model: Model, private val listFetcher: () -> Unit) : Builder<Region> {
    override fun build(): Region = BorderPane().apply {
        center = createTable()
        bottom = createLookup()
        padding = Insets(15.0)
    }

    private fun createLookup(): Region = HBox(10.0).apply {
        children += Label("List Name:")
        children += TextField().apply {
            textProperty().bindBidirectional(model.listName)
        }
        children += Button("Fetch").apply {
            setOnAction { listFetcher.invoke() }
        }
        padding = Insets(5.0)
    }

    private fun createTable(): Node = TableView<GroceryModel>().apply {
        columns += TableColumn<GroceryModel, String>("Item Name").apply {
            setCellValueFactory { p -> p.value.name }
        }
        columns += TableColumn<GroceryModel, Department>("Department").apply {
            setCellValueFactory { p -> p.value.department }
        }
        columns += TableColumn<GroceryModel, Int>("Quantity").apply {
            setCellValueFactory { p -> p.value.quantity }
        }
        items = model.groceryList
        columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY_LAST_COLUMN
    }
}