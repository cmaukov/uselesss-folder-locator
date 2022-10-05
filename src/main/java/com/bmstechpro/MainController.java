package com.bmstechpro;
/* uselesss-folder-locator
 * @created 09/30/2022
 * @author Konstantin Staykov
 */

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController implements Initializable {

    @FXML
    private ListView<Path> filesList;

    @FXML
    private Button renameFolderBtn;

    @FXML
    private Label rootFolderPath;


    @FXML
    private ListView<Path> uselessFolderList;

    @FXML
    private Label msg;

    private UselessFolderLocator uselessFolderLocator;

    private ExecutorService executorService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clear();
        renameFolderBtn.disableProperty().bind(Bindings.isEmpty(uselessFolderList.getItems()));
        executorService = Executors.newSingleThreadExecutor();
    }

    @FXML
    void renameFolders(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to rename " + uselessFolderLocator.getUselessFolders().size() + " folders ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            if (uselessFolderLocator != null) {
                uselessFolderLocator.renameFolders();
                uselessFolderList.getItems().clear();
                msg.setText("" + uselessFolderLocator.getUselessFolders().size() + " folders renamed");
            }
        }


    }

    @FXML
    void selectRootFolder(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(rootFolderPath.getScene().getWindow());
        if (file != null) {

            clear();
            rootFolderPath.setText(file.getPath());
            executorService.execute(() -> {
                try {
                    uselessFolderLocator = new UselessFolderLocator(file.getPath());
                    Platform.runLater(()->{
                        List<Path> files = uselessFolderLocator.getFilesList();
                        List<Path> uselessFolders = uselessFolderLocator.getUselessFolders();
                        filesList.getItems().addAll(files);
                        uselessFolderList.getItems().addAll(uselessFolders);
                    });

                } catch (IOException e) {
                    msg.setText("Ops... Check permissions");
                    uselessFolderLocator = null;
                }
            });

        } else {
            clear();
        }

    }

    private void clear() {
        rootFolderPath.setText("");
        filesList.getItems().clear();
        uselessFolderList.getItems().clear();
        msg.setText("");
    }


}
