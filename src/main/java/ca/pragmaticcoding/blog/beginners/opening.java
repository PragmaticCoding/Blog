package ca.pragmaticcoding.blog.beginners;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class opening extends Application {
    // first scene (buttons
    Label gameTitle = new Label("Basil's Cat Game :3");

    // new game buttons
    TextField putNameHere = new TextField();

    Label putNameTxt = new Label("Name:");
    Button nameOkButton = new Button("OK");

    VBox titleVBox = new VBox();
    HBox titleHBox = new HBox();
    GridPane titleGroup = new GridPane();
    Button backBtn = new Button("BACK");

    VBox title = new VBox();
    Button exitBtn = new Button();

    Group titleGroupSec = new Group();

    boolean buttonClicked = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // needs buttons to select what to do and also a text field to store a name
        gameTitle.setFont(Font.font(STYLESHEET_CASPIAN, 40));

        Button playBtn = new Button("NEW");
        playBtn.setFont(Font.font(30));
        playBtn.setBackground(Background.EMPTY);
        Button loadBtn = new Button("LOAD");
        loadBtn.setFont(Font.font(30));
        loadBtn.setBackground(Background.EMPTY);
        Button exitBtn = new Button("EXIT");
        exitBtn.setFont(Font.font(30));
        exitBtn.setBackground(Background.EMPTY);

        Button exitBtnSec = new Button("EXIT");
        exitBtnSec.setFont(Font.font(30));
        exitBtnSec.setBackground(Background.EMPTY);

        VBox btnVBox = new VBox();
        btnVBox.getChildren().add(0, gameTitle);
        btnVBox.getChildren().add(1, playBtn);
        btnVBox.getChildren().add(2, loadBtn);
        btnVBox.getChildren().add(3, exitBtn);
        btnVBox.setAlignment(Pos.TOP_CENTER);
        btnVBox.setSpacing(20);

        titleGroup.getChildren().remove(titleHBox);
        titleGroup.getChildren().remove(titleVBox);
        titleGroup.getChildren().add(btnVBox);
        titleGroup.setAlignment(Pos.TOP_CENTER);
        titleGroup.setPadding(new Insets(50));

        /**
         * maybe should change to say "NEW"? anyways, make them pick a name (move name
         * feature to here & show a quick text based tutorial that does some waiting
         * liek im sayin it to the player :3
         *
         * make them roll for a cat nd name the cat. save the cat and it's features to a
         * file that is specific to this player.
         *
         * make it so they can access a list to see player info (name) and cat info(how
         * many cats, names, breeds, traits, colors)
         */
        playBtn.setOnMouseClicked(playBtnClickedEvent -> {
            btnVBox.getChildren().remove(loadBtn);
            btnVBox.getChildren().remove(exitBtn);
            btnVBox.getChildren().remove(playBtn);


            exitBtn.setAlignment(Pos.TOP_LEFT);

            gameTitle.setFont(Font.font(STYLESHEET_CASPIAN, 40));

            putNameTxt.setFont(Font.font(STYLESHEET_CASPIAN, 20));

            putNameHere.setEditable(true);

            nameOkButton.setFont(Font.font(10));

            title.getChildren().add(putNameTxt);
            title.getChildren().add(exitBtn);
            title.getChildren().add(gameTitle);

            title.setSpacing(10);
            title.setAlignment(Pos.TOP_CENTER);

            titleHBox.getChildren().add(putNameHere);
            titleHBox.getChildren().add(nameOkButton);
            titleHBox.setPadding(new Insets(100));
            titleHBox.setAlignment(Pos.CENTER);

            titleGroup.getChildren().add(backBtn);
            titleGroup.getChildren().add(title);
            titleGroup.getChildren().add(titleHBox);


        });

        /**
         * here, u will probably have to implement something more complex than a file
         * writer if you decide to use photos or graphics to make ur kitties :P
         *
         * call the names of players (only players that have pressed the PLAY button &
         * gotten a cat.. show the amt of cats they have next to their name :P)
         *
         * if they click on the button that has their name/amt of cats then make open
         * that save file in the stage
         */
        loadBtn.setOnMouseClicked(loadBtnClickedEvent -> {

        });

        exitBtn.setOnMouseClicked(exitBtnClickedEvent -> {
            primaryStage.close();
        });

        Scene scene = new Scene(titleGroup, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("love u <3");
        primaryStage.show();

    }
}