package ca.pragmaticcoding.blog.styleable

import javafx.application.Application
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage

class Styleable6 : Application() {

    val partsAmount: DoubleProperty = SimpleDoubleProperty(1783.98)
    val labourAmount: DoubleProperty = SimpleDoubleProperty(300.0)
    val taxAmount: DoubleProperty = SimpleDoubleProperty(103.76)
    val discountAmount: DoubleProperty = SimpleDoubleProperty(-10.0)
    val totalAmount: DoubleProperty = SimpleDoubleProperty(2177.74)
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent()).apply {
            Styleable3::class.java.getResource("styleable6.css")?.toString()?.let { stylesheets += it }
        }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        center = customBox()
        padding = Insets(10.0)
    }

    private fun customBox(): Region = VBox(7.0).apply {
        println("Got here")
        children += HBox().apply {
            children += Label("Parts:").apply { minWidth = 150.0 }
            children += DecimalLabel(partsAmount)
        }
        children += HBox().apply {
            children += Label("Labour:").apply { minWidth = 150.0 }
            children += DecimalLabel(labourAmount)
        }
        children += HBox().apply {
            children += Label("Tax:").apply { minWidth = 150.0 }
            children += DecimalLabel(taxAmount)
        }
        children += HBox().apply {
            children += Label("Discount:").apply { minWidth = 150.0 }
            children += DecimalLabel(discountAmount)
        }
        children += HBox().apply {
            children += Label("Total:").apply { minWidth = 150.0 }
            children += DecimalLabel(totalAmount)
        }
    }
}

fun main() = Application.launch(Styleable6::class.java)