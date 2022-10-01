package com.bmstechpro;
/* uselesss-folder-locator
 * @created 09/30/2022
 * @author Konstantin Staykov
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));
        Scene scene = new Scene(parent);

        primaryStage.setTitle("Useless Folder Locator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
