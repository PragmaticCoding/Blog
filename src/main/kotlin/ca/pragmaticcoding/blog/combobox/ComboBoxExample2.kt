package ca.pragmaticcoding.blog.combobox;

import ca.pragmaticcoding.widgetsfx.addWidgetStyles
import javafx.application.Application
import javafx.beans.binding.StringBinding
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.util.function.Consumer


class ComboBoxExample2 : Application() {

    private val animalValue: ObjectProperty<Animal> = SimpleObjectProperty()
    private val nameValue: ObjectProperty<String> = SimpleObjectProperty()

    override fun start(stage: Stage) {
        val scene = Scene(createContent(), 400.0, 300.0).apply {
            addWidgetStyles()
        }
        stage.scene = scene
        stage.show()
    }

    private fun createContent(): Region = VBox(20.0).apply {
        children += Label().apply {
            textProperty().bind(FavouriteAnimalBinding(animalValue, nameValue))
        }
        children += HBox(14.0).apply {
            children += ComboBox<Animal>().apply {
                items += listOf(Animal.BEAR, Animal.OWL, Animal.BIRD, Animal.SHARK, Animal.HEDGEHOG, Animal.LOBSTER)
                animalValue.bind(valueProperty())
                setCellFactory { createCell() }
                buttonCell = createCell()
            }
            children += ComboBox<String>().apply {
                val zooAnimals = populateZooAnimals()
                itemsProperty().bind(animalValue.map { zooAnimals[it] })
                nameValue.bind(valueProperty())
                placeholder = Label("  Pick an Animal first  ")
                isEditable = true
            }
        }
        padding = Insets(40.0)
    }

    private fun populateZooAnimals(): Map<Animal, ObservableList<String>> = mapOf<Animal, ObservableList<String>>(
        Animal.OWL to FXCollections.observableArrayList("Hoodini", "Olive Owl", "Whoolio", "Barney"),
        Animal.SHARK to FXCollections.observableArrayList("Striper", "Steve", "Freddy", "Sushi"),
        Animal.LOBSTER to FXCollections.observableArrayList("Clawdia", "Kevin", "Butter"),
        Animal.BEAR to FXCollections.observableArrayList("Gummi", "Paddington", "Baloo", "Ted"),
        Animal.BIRD to FXCollections.observableArrayList("Tweety", "Comet", "Chickpea", "Cracker"),
        Animal.HEDGEHOG to FXCollections.observableArrayList("Sonic", "Spike", "Whoolio", "Herbert")
    )


    private fun createCell() = ListCell<Animal>().apply {
        val imageView = ImageView()
        itemProperty().subscribe(Consumer {
            imageView.image = it?.image
            graphic = imageView
            text = it?.animalName
        })
    }
}

class FavouriteAnimalBinding(private val animal: ObservableValue<Animal?>, val name: ObservableValue<String?>) :
    StringBinding() {

    init {
        super.bind(animal, name)
    }

    override fun computeValue(): String {
        val favString = "My favourite animal at the zoo is"
        return animal.value?.let { animalType ->
            name.value?.let { animalName -> "$favString $animalName the $animalType" } ?: "$favString a $animalType"
        } ?: "I haven't picked a favourite yet"
    }
}

fun main() = Application.launch(ComboBoxExample2::class.java)