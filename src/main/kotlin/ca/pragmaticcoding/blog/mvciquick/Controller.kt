package ca.pragmaticcoding.blog.mvciquick

import javafx.concurrent.Task
import javafx.scene.layout.Region
import javafx.util.Builder

class Controller {
    private val model = Model()
    private val interactor = Interactor(model)
    private val viewBuilder: Builder<Region> = ViewBuilder(model) { fetchList() }

    private fun fetchList() {
        Thread(object : Task<List<GroceryItem>>() {
            override fun call() = interactor.getList()
        }.apply { setOnSucceeded { _ -> interactor.updateModelAfterFetch(get()) } }).apply {
            isDaemon = true
            start()
        }
    }

    fun getView(): Region = viewBuilder.build()
}