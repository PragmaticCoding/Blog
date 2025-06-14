package ca.pragmaticcoding.blog.mvciquick

class GroceryListService {
    fun fetchList(listName: String): List<GroceryItem> {
        Thread.sleep(2000)
        return listOf(
            GroceryItem("Celery", Department.VEGGIES, 10),
            GroceryItem("Milk", Department.DAIRY, 4),
            GroceryItem("Grapes", Department.FRUITS, 230)
        )
    }
}

data class GroceryItem(val itemName: String, val theDepartment: Department, val howMany: Int)