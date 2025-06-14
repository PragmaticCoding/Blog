package ca.pragmaticcoding.blog.mvciquick

class Interactor(private val model: Model) {

    private val listService = GroceryListService()

    fun getList(): List<GroceryItem> {
        return if (model.listName.value.isEmpty()) {
            listService.fetchList("DEFAULT")
        } else {
            listService.fetchList(model.listName.value)
        }
    }

    fun updateModelAfterFetch(groceries: List<GroceryItem>) {
        model.groceryList.setAll(groceries.map { createGroceryModel(it) })
    }

    private fun createGroceryModel(groceryItem: GroceryItem) = GroceryModel().apply {
        name.value = groceryItem.itemName
        department.value = groceryItem.theDepartment
        quantity.value = groceryItem.howMany
    }
}