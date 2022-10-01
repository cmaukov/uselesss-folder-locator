package com.bmstechpro;
/* uselesss-folder-locator
 * @created 09/30/2022
 * @author Konstantin Staykov
 */

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

public class MainController implements Initializable {

    @FXML
    private ListView<Path> filesList;

    @FXML
    private Button renameFolderBtn;

    @FXML
    private Label rootFolderPath;

    @FXML
    private Button selectRootFolderBtn;

    @FXML
    private ListView<Path> uselessFolderList;

    @FXML
    private Label msg;

   private UselessFolderLocator uselessFolderLocator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clear();
        renameFolderBtn.disableProperty().bind(Bindings.isEmpty(uselessFolderList.getItems()));
    }

    @FXML
    void renameFolders(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Are you sure you want to rename "+ uselessFolderLocator.getUselessFolders().size()+" folders ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            System.out.println("rename folder");

            if(uselessFolderLocator!=null){
                uselessFolderLocator.renameFolders();
                uselessFolderList.getItems().clear();
                msg.setText(""+uselessFolderLocator.getUselessFolders().size()+" folders renamed");
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }




    }

    @FXML
    void selectRootFolder(ActionEvent event) {
        System.out.println("select root");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(rootFolderPath.getScene().getWindow());
        if (file != null) {

            clear();
            rootFolderPath.setText(file.getPath());
            try {
                uselessFolderLocator = new UselessFolderLocator(file.getPath());
                List<Path> files = uselessFolderLocator.getFilesList();
                List<Path> uselessFolders = uselessFolderLocator.getUselessFolders();
                filesList.getItems().addAll(files);
                uselessFolderList.getItems().addAll(uselessFolders);
            } catch (IOException e) {
                msg.setText("Ops... Check permissions");
                uselessFolderLocator = null;
            }
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
