package ca.pragmaticcoding.blog.pseudo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EnumPseudoApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent(), 300, 240);
        scene.getStylesheets().add(EnumPseudoApplication.class.getResource("test.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private Region createContent() {
        VBox results = new VBox(20);
        Label label = new Label("This is the label");
        label.getStyleClass().add("status-label");
        EnumPseudoClassProperty<StatusEnum> status = new EnumPseudoClassProperty<>(label, StatusEnum.FAILED);
        results.getChildren().addAll(label, createButton("Failed", status, StatusEnum.FAILED),
                createButton("Normal", status, StatusEnum.NORMAL),
                createButton("Warning", status, StatusEnum.WARNING),
                createButton("Error", status, StatusEnum.ERROR));
        return results;
    }

    private Region createButton(String text, EnumPseudoClassProperty<StatusEnum> property, StatusEnum newValue) {
        Button button = new Button(text);
        button.setOnAction(evt -> property.set(newValue));
        return button;
    }
}
