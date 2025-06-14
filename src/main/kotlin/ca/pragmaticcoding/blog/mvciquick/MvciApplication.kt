package ca.pragmaticcoding.blog.mvciquick

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class MvciApplication : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(Controller().getView(), 500.0, 400.0)
        stage.show()
    }
}

fun main() = Application.launch(MvciApplication::class.java)