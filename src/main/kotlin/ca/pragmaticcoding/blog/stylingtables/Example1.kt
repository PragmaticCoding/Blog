package ca.pragmaticcoding.blog.stylingtables

import ca.pragmaticcoding.widgetsfx.hoverLookup
import javafx.application.Application
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.css.PseudoClass
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.stage.Stage
import kotlin.random.Random

class Example1 : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 550.0, 300.0).apply {
            Example0::class.java.getResource("Example0.css")?.toString()?.let {
                stylesheets += it
                println("added")
            }
        }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        center = TableView<TableData1>().apply {
            columns += TableColumn<TableData1, String>("Column A").apply {
                setCellValueFactory { it.value.value1 }
                styleClass += "a-column"
            }
            columns += TableColumn<TableData1, String>("B and C").apply {
                styleClass += "bc-nested-column"
                columns += TableColumn<TableData1, String>("Column B").apply {
                    setCellValueFactory { it.value.value2 }
                    styleClass += "bc-column"
                }
                columns += TableColumn<TableData1, String>("Column C").apply {
                    setCellFactory { CustomTableCell1() }
                    setCellValueFactory { it.value.value3 }
                    styleClass += "bc-column"
                }
            }
            columns += TableColumn<TableData1, Boolean>("Column D").apply {
                setCellValueFactory { it.value.value4 }
                styleClass += "d-column"
            }
            setRowFactory { CustomTableRow() }
            isTableMenuButtonVisible = true
            columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY_LAST_COLUMN
            for (x in 1..30) items += TableData1("A", "B", "C", Random.nextBoolean())
        }.hoverLookup()
        bottom = HBox(20.0).apply {
            children += TextField()
            padding = Insets(10.0)
        }
        padding = Insets(20.0)
    }
}

class CustomTableRow : TableRow<TableData1>() {
    companion object PseudoClasses {
        val specialPC = PseudoClass.getPseudoClass("special")
    }

    override fun updateItem(newValue: TableData1?, empty: Boolean) {
        super.updateItem(newValue, empty)
        if (empty || newValue == null) {
            this.pseudoClassStateChanged(specialPC, false)
        } else {
            pseudoClassStateChanged(specialPC, newValue.value4.value)
        }
    }
}

class CustomTableCell1 : TableCell<TableData1, String>() {
    override fun updateItem(p0: String?, empty: Boolean) {
        super.updateItem(p0, empty)
        text = p0
    }
}

class TableData1(initVal1: String, initVal2: String, initVal3: String, initVal4: Boolean) {
    val value1: StringProperty = SimpleStringProperty(initVal1)
    val value2: StringProperty = SimpleStringProperty(initVal2)
    val value3: StringProperty = SimpleStringProperty(initVal3)
    val value4: BooleanProperty = SimpleBooleanProperty(initVal4)
}

fun main() = Application.launch(Example1::class.java)