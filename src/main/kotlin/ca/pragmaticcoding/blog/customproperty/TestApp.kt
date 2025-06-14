package ca.pragmaticcoding.blog.customproperty

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.util.Builder


class TestApplication1 : Application() {
    override fun start(stage: Stage) {
        with(stage) {
            scene = Scene(TestScreenBuilder().build(), 400.0, 400.0)
            show()
            centerOnScreen()
        }
    }
}

class TestScreenBuilder : Builder<Region> {
    override fun build(): Region = VBox(10.0).apply {
        val customer1 = CustomerImpl().apply {
            setFName("Fred")
            setLName("Smith")
        }
        val customer2 = CustomerImpl().apply {
            setFName("George")
            setLName("Jones")
        }
        val customerProperty: CustomerProperty = SimpleCustomerProperty(customer1)
        children += Label().apply {
            textProperty().bind(customerProperty.fNameProperty().map { "Current Customer FName: $it" })
        }
        children += Label().apply {
            textProperty().bind(customerProperty.fullName().map { "Current Customer Full Name: $it" })
        }
        children += HBox(10.0).apply {
            children += Label("Customer1: ")
            children += Label().apply {
                textProperty().bind(customer1.fNameProperty())
            }
        }
        children += HBox(10.0).apply {
            children += Label("Customer2: ")
            children += Label().apply {
                textProperty().bind(customer2.fNameProperty())
            }
        }
        var swapper = true
        children += Button("Swap Customer").apply {
            setOnAction {
                customerProperty.set(if (swapper) customer2 else customer1)
                swapper = swapper.not()
            }
        }
        val nameList = listOf("Albert", "Vinny", "Bob", "Aloysius")
        val nameList2 = listOf("Jane", "Anne", "Barb", "Zoey")
        var counter = 0
        var counter2 = 0
        children += Button("Change Property Name").apply {
            setOnAction { customerProperty.setFName(nameList[counter++]) }
        }
        children += Button("Change Cust1 Name").apply {
            setOnAction { customer1.setFName(nameList2[counter2++]) }
        }
        padding = Insets(30.0)
    }

}


fun main() {
    Application.launch(TestApplication1::class.java)
}