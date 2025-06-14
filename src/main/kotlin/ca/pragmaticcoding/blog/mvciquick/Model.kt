package ca.pragmaticcoding.blog.mvciquick

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class Model {
    val listName: StringProperty = SimpleStringProperty("")
    val groceryList: ObservableList<GroceryModel> = FXCollections.observableArrayList()
}

class GroceryModel {
    val name: StringProperty = SimpleStringProperty("")
    val quantity: ObjectProperty<Int> = SimpleObjectProperty(0)
    val department: ObjectProperty<Department> = SimpleObjectProperty()
}

enum class Department {
    DAIRY, FRUITS, VEGGIES, MEATS, BAKERY;
}