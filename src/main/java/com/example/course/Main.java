package com.example.course;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("hello-view.fxml"));
        Pane root = fxmlLoader.load();

        Action.setRoot(root);
        Action.buildWall();


        root = Action.getRoot();
        Scene scene = new Scene(root, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        stage.setResizable(false);
        stage.setTitle("Course work ASD!");
        stage.setScene(scene);
        stage.show();
        root.setFocusTraversable(true);
        root.requestFocus();
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W -> Action.movePlayer(0);
                case A -> Action.movePlayer(1);
                case S -> Action.movePlayer(2);
                case D -> Action.movePlayer(3);
            }
        });
    }

    public static void main(String[] args) {
        launch();

    }

}