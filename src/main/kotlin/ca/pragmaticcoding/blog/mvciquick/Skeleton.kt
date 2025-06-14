package ca.pragmaticcoding.blog.mvciquick

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.Region
import javafx.stage.Stage
import javafx.util.Builder

class SkeletonModel

class SkeletonInteractor(private var model: SkeletonModel)

class SkeletonViewBuilder(private var model: SkeletonModel) : Builder<Region> {
    override fun build(): Region {
        TODO("Not yet implemented")
    }
}

class SkeletonController {

    private val model = SkeletonModel()
    private val interactor = SkeletonInteractor(model)
    private val viewBuilder: Builder<Region> = SkeletonViewBuilder(model)

    fun getView() = viewBuilder.build()
}

class SkeletonApp : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(SkeletonController().getView())
        stage.show()
    }
}

fun main() = Application.launch(SkeletonApp::class.java)