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


class TestApplication2 : Application() {
    override fun start(stage: Stage) {
        with(stage) {
            scene = Scene(TestScreenBuilder2().build(), 400.0, 400.0)
            show()
        }
    }
}

class TestScreenBuilder2 : Builder<Region> {
    override fun build(): Region = VBox(10.0).apply {
        val customer1 = CustomerImpl().apply {
            setFName("Fred")
            setLName("Smith")
            setAge(30)
        }
        val customer2 = CustomerImpl().apply {
            setFName("George")
            setLName("Jones")
            setAge(40)
            fNameProperty().subscribe(Runnable {
                val x = 5
                println("Customer2 FName Invalidated")
            })
        }
        val customer3 = CustomerImpl().apply {
            setFName("Bob")
            setLName("Brown")
            setAge(25)
        }
        println("Start Customer1: ${customer1.getLName()}")
        println("Start Customer2: ${customer2.getLName()}")
        println("Start Customer3: ${customer3.getLName()}")

        val customerPropertyA: CustomerProperty = SimpleCustomerProperty(customer1)
        val customerPropertyB: CustomerProperty = SimpleCustomerProperty(customer2)

        println("Start CustomerA: ${customerPropertyA.getLName()}")
        println("Start CustomerB: ${customerPropertyB.getLName()}")
        customerPropertyB.subscribe { newVal ->
            println("Customer B: ${newVal.fullName().value}")
            println("Customer2: ${customer2.getLName()}")
        }
        customerPropertyA.subscribe { newVal ->
            println("Customer A: ${newVal.fullName().value}")
            println("Customer2: ${customer2.getLName()}")
        }
        val oldestBinding = object : CustomerBinding() {
            init {
                println("In the OldestBinding Init")
                super.bind(customerPropertyA, customerPropertyB)
            }

            override fun computeValue(): CustomerModel {
                println("Computing")
                println("Inside B: ${customerPropertyB.getAge()}")
                println("More Inside")
                println("Inside A: ${customerPropertyA.getAge()}")
                println("More Inside")
                val result =
                    if (customerPropertyA.getAge() >= customerPropertyB.getAge()) customerPropertyA.value else customerPropertyB.value
                println("Still Inside: $result")
                return result
            }
        }
        children += Label().apply {
            textProperty().bind(oldestBinding.lNameProperty())
        }
        children += HBox(10.0).apply {
            children += Label("CustomerA: ")
            children += Label().apply {
                textProperty().bind(customerPropertyA.fullName())
            }
        }
        children += HBox(10.0).apply {
            children += Label("CustomerB: ")
            children += Label().apply {
                textProperty().bind(customerPropertyB.fullName())
            }
        }
        var swapper = true
        children += Button("Swap Customer").apply {
            setOnAction {
                customerPropertyB.set(if (swapper) customer3 else customer2)
                swapper = swapper.not()
            }
        }
        padding = Insets(30.0)
    }

}


fun main() {
    Application.launch(TestApplication2::class.java)
}