package ca.pragmaticcoding.blog.stylingtables

import ca.pragmaticcoding.widgetsfx.hoverLookup
import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.stage.Stage

class Example0 : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 350.0, 300.0).apply {
            Example0::class.java.getResource("Example0.css")?.toString()?.let {
                stylesheets += it
                println("added")
            }
        }
        stage.show()
    }

    private fun createContent(): Region = BorderPane().apply {
        center = TableView<TableData>().apply {
            columns += TableColumn<TableData, String>("Column A").apply {
                setCellFactory { CustomTableCell() }
                setCellValueFactory { it.value.value1 }
                styleClass += "a-column"
            }
            columns += TableColumn<TableData, String>("B and C").apply {
                styleClass += "bc-nested-column"
                columns += TableColumn<TableData, String>("Column B").apply {
                    setCellFactory { CustomTableCell() }
                    setCellValueFactory { it.value.value2 }
                    styleClass += "bc-column"
                }
                columns += TableColumn<TableData, String>("Column C").apply {
                    setCellFactory { CustomTableCell() }
                    setCellValueFactory { it.value.value3 }
                    styleClass += "bc-column"
                }
            }
            columns += TableColumn<TableData, String>("Column D").apply {
                setCellFactory { CustomTableCell() }
                setCellValueFactory { it.value.value4 }
            }
            isTableMenuButtonVisible = true
//            columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY_LAST_COLUMN
            for (x in 1..30) items += TableData("A", "B", "C", "D")
        }.hoverLookup()
        bottom = HBox().apply {
            children += TextField()
            padding = Insets(10.0)
        }
        padding = Insets(20.0)
    }
}

class CustomTableCell : TableCell<TableData, String>() {
    override fun updateItem(p0: String?, p1: Boolean) {
        super.updateItem(p0, p1)
        text = p0
    }
}

class TableData(initVal1: String, initVal2: String, initVal3: String, initVal4: String) {
    val value1: StringProperty = SimpleStringProperty(initVal1)
    val value2: StringProperty = SimpleStringProperty(initVal2)
    val value3: StringProperty = SimpleStringProperty(initVal3)
    val value4: StringProperty = SimpleStringProperty(initVal4)
}

fun main() = Application.launch(Example0::class.java)