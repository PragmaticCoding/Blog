package ca.pragmaticcoding.blog.combobox

import ca.pragmaticcoding.widgetsfx.addWidgetStyles
import javafx.application.Application
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.util.function.Consumer


class ComboBoxExample1 : Application() {

    private val comboBoxValue: ObjectProperty<Animal> = SimpleObjectProperty()

    override fun start(stage: Stage) {
        val scene = Scene(createContent(), 280.0, 300.0).apply {
            addWidgetStyles()
        }
        stage.scene = scene
        stage.show()
    }

    private fun createContent(): Region = VBox(20.0).apply {
        children += Label().apply {
            textProperty().bind(comboBoxValue.map { "The selected value is: $it" }.orElse("No value has been selected"))
        }
        children += ComboBox<Animal>().apply {
            items += listOf(Animal.BEAR, Animal.OWL, Animal.BIRD, Animal.SHARK, Animal.HEDGEHOG, Animal.LOBSTER)
            comboBoxValue.bind(valueProperty())
            setCellFactory { createCell() }
            buttonCell = createCell()
        }
        padding = Insets(40.0)
    }

    private fun createCell() = ListCell<Animal>().apply {
        val imageView = ImageView()
        itemProperty().subscribe(Consumer {
            imageView.image = it?.image
            graphic = imageView
            text = it?.animalName
        })
    }
}


enum class Animal(val animalName: String, imageName: String) {
    BEAR("Bear", "bear.png"),
    BIRD("Bird", "bird.png"),
    HEDGEHOG("Hedgehog", "hedgehog.png"),
    LOBSTER("Lobster", "lobster.png"),
    OWL("Owl", "owl.png"),
    SHARK("Shark", "shark.png");

    val image: Image = Image(this::class.java.getResource(imageName)?.toExternalForm())

    override fun toString(): String {
        return animalName
    }
}

fun main() = Application.launch(ComboBoxExample1::class.java)