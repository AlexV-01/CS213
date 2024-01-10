package photos.controllers;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import photos.Album;
import photos.Photo;
import photos.Utility;

/**
 * Our controller class for the photo transfer window.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class AlbumDestinationController {
    /**
     * The drop-down menu from which the user can select the destination album.
     */
    @FXML ChoiceBox<Album> list;

    /**
     * The button to cancel the transfer action and close out of this window.
     */
    @FXML Button cancel;

    /**
     * The button to confirm the transfer action.
     */
    @FXML Button confirm;

    /**
     * Our list of destination albums.
     */
    ObservableList<Album> obsList;

    /**
     * The source album.
     */
    Album album;

    /**
     * The photo to be moved.
     */
    Photo photo;

    /**
     * The current album controller.
     */
    AlbumController albumController = Utility.currentAlbumController;
    
    private Utility.TransferMode transferMode;

    /**
     * The method to initialize this scene.
     */
    public void initialize() {
        photo = Utility.currentPhoto;
        album = Utility.currentAlbum;
        transferMode = Utility.currentMode;
        list.getSelectionModel().selectedIndexProperty().addListener(e -> updateUI());
        update();
        Utility.openedDestinationWindows.add(this);
        updateUI();
    }

    /**
     * Updates the <code>obsList</code> according to the state of all albums.
     */
    public void update() {
        ArrayList<Album> copy = new ArrayList<Album>();
        for (Album a : Utility.currentUser.getAlbums()) {
            if (a != album) copy.add(a);
        }

        obsList = FXCollections.observableArrayList(copy);
        list.setItems(obsList);
    }

    /**
     * Updates the UI according to the status of the program.
     */
    public void updateUI() {
        confirm.setDisable(list.getSelectionModel().getSelectedIndex() == -1);
    }

    /**
     * Confirms the photo transfer.
     */
    public void confirm() {
        if (list.getSelectionModel().getSelectedIndex() == -1) return;
        Album a = list.getSelectionModel().getSelectedItem();
        if (a.getPhotos().contains(photo)) return;

        int ind = Utility.users.indexOf(Utility.currentUser);
        int index = Utility.photos.get(ind).indexOf(photo);
        Utility.useCount.get(ind).set(index, Utility.useCount.get(ind).get(index) + 1);

        a.addPhoto(photo);
        
        if (transferMode == Utility.TransferMode.MOVE) {
            albumController.removePhoto();
        }

        Stage stage = (Stage)confirm.getScene().getWindow();
        stage.close();
        Utility.currentUserController.update();
    }

    /**
     * Cancels the transfer operation.
     */
    public void cancel() {
        Stage stage = (Stage)cancel.getScene().getWindow();
        stage.close();
    }

    /**
     * Closes this window.
     */
    public void close() {
        Stage destinationStage = (Stage) confirm.getScene().getWindow();
        destinationStage.close();
    }
}