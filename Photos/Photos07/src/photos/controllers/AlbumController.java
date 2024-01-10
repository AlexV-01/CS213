package photos.controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import photos.Album;
import photos.Photo;
import photos.Utility;

/**
 * Our controller class for an album page.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class AlbumController {
    /**
     * The list of all photos in this album.
     */
    @FXML ListView<Photo> photos;

    /**
     * The button to add a photo to this album.
     */
    @FXML Button addPhoto;

    /**
     * The button to remove a photo from this album.
     */
    @FXML Button removePhoto;

    /**
     * The button to update the caption of a selected photo.
     */
    @FXML Button updateCaption;

    /**
     * A field for the user to enter a new caption for a selected photo.
     */
    @FXML TextField caption;

    /**
     * The button to view a selected photo in this album.
     */
    @FXML Button viewPhoto;

    /**
     * The button to copy a selected photo to another album.
     */
    @FXML Button copyPhoto;

    /**
     * the button to move a selected photo to another album.
     */
    @FXML Button movePhoto;

    /**
     * The button to search for photos that satisfy selected criteria.
     */
    @FXML Button search;

    /**
     * The button to close this page.
     */
    @FXML Button back;

    /**
     * A <code>Text</code> object that displays the name of this album.
     */
    @FXML Text name;

    private ObservableList<Photo> obsList;
    private Album album;
    private Stage stage;
    
    /**
     * The method to initialize this scene.
     */
    public void initialize() {
        album = Utility.openedAlbums.get(Utility.openedAlbums.size() - 1);
        stage = Utility.albumStages.get(Utility.albumStages.size() - 1);
        obsList = FXCollections.observableArrayList(album.getPhotos());
        photos.setItems(obsList);
        name.setText(album.getName());
        Utility.openedAlbumWindows.add(this);
        stage.setOnCloseRequest(e -> clear());
        photos.getSelectionModel().selectedIndexProperty().addListener(e -> load());
        photos.setCellFactory(listView -> new ListCell<Photo>() {
            private ImageView imageView = new ImageView();
            @Override
            public void updateItem(Photo str, boolean empty) {
                super.updateItem(str, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Image image = getImage(str);
                    imageView.setImage(image);
                    setText(str.toString() + str.getListCaption());
                    setGraphic(imageView);
                }
            }
        });
        load();
    }

    private Image getImage(Photo str) {
        int ind = Utility.users.indexOf(Utility.currentUser);
        String f = Utility.directories.get(ind).get(Utility.photos.get(ind).indexOf(str));
        if (f.substring(0, 1).equals(File.separator)) f = Utility.workingDirectory + f;
        Image img = new Image(f, 32, 32, false, false);
        return img;
    }

    /**
     * Closes this page.
     */
    public void back() {
        Utility.openedAlbums.remove(album);
        Utility.albumStages.remove(stage);
        Stage albumStage = (Stage) back.getScene().getWindow();
        albumStage.close();
    }

    /**
     * Keeps the UI updated according to the current state of the program.
     */
    public void load() {
        int index = photos.getSelectionModel().getSelectedIndex();
        if (index == -1) { 
            caption.setText("");
            viewPhoto.setDisable(true);
            copyPhoto.setDisable(true);
            removePhoto.setDisable(true);
            movePhoto.setDisable(true);
            caption.setDisable(true);
            updateCaption.setDisable(true);
        }
        else {
            caption.setText(album.getPhotos().get(index).getCaption());
            viewPhoto.setDisable(false);
            copyPhoto.setDisable(false);
            removePhoto.setDisable(false);
            movePhoto.setDisable(false);
            caption.setDisable(false);
            updateCaption.setDisable(false);
            search.setDisable(false);
        }
        search.setDisable(album.getPhotos().size() == 0);
    }

    /**
     * Closes this album and removes it from <code>openedAlbumWindows</code>.
     */
    public void clear() {
        Utility.openedAlbumWindows.remove(this);
        back();
    }

    /**
     * Adds a selected photo to this album.
     */
    public void addPhoto() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new ExtensionFilter("Photo", "*.BMP", "*.GIF", "*.JPEG", "*JPG", "*.PNG"));
        List<File> file = chooser.showOpenMultipleDialog(stage);
        if (file == null) return;
        for (File f : file) { addEachPhoto(f); }
        for (PhotoController pc : Utility.openedPhotoWindows) pc.update();
        Utility.currentUserController.update();
    }

    private void addEachPhoto(File file) {
        Photo photo;
        int ind = Utility.users.indexOf(Utility.currentUser);
        String path = localizeDirectory(file.getPath());
        if (!Utility.directories.get(ind).contains(path)) {
            photo = new Photo(file.getName());
            Utility.photos.get(ind).add(photo);
            Utility.files.get(ind).add(file);
            Utility.useCount.get(ind).add(1);
            Utility.directories.get(ind).add(path);
        }
        else {
            int index = Utility.directories.get(ind).indexOf(path);
            photo = Utility.photos.get(ind).get(index);
            if (album.getPhotos().contains(photo)) return;
            Utility.useCount.get(ind).set(index, Utility.useCount.get(ind).get(index) + 1);
        }
        album.addPhoto(photo);
        photo.albums.add(album);
        obsList.add(photo);
        photos.setItems(obsList);
        photos.getSelectionModel().clearAndSelect(obsList.size()-1);
    }
    private String localizeDirectory(final String filePath) {
        if (filePath.contains(Utility.workingDirectory)) {
            int ind = Utility.workingDirectory.length() - filePath.indexOf(Utility.workingDirectory);
            String newDir = filePath.substring(ind, filePath.length());
            return newDir;
        }
        return filePath;
    }

    /**
     * Removes a selected photo from this album.
     */
    public void removePhoto() {
        int photoIndex = photos.getSelectionModel().getSelectedIndex();
        if (photoIndex == -1) return;
        Photo p = album.getPhotos().get(photoIndex);
        int ind = Utility.users.indexOf(Utility.currentUser);
        int globalPhotoIndex = Utility.photos.get(ind).indexOf(p);
        album.removePhoto(p);
        p.albums.remove(album);
        obsList.remove(p);
        photos.setItems(obsList);
        int currentCount = Utility.useCount.get(ind).get(globalPhotoIndex);
        if (currentCount == 1) {
            Utility.photos.get(ind).remove(globalPhotoIndex);
            Utility.files.get(ind).remove(globalPhotoIndex);
            Utility.useCount.get(ind).remove(globalPhotoIndex);
            Utility.directories.get(ind).remove(globalPhotoIndex);
        } else {
            Utility.useCount.get(ind).set(globalPhotoIndex, Utility.useCount.get(ind).get(globalPhotoIndex) - 1);
        }
        for (PhotoController pc : Utility.openedPhotoWindows) pc.update();
        Utility.currentUserController.update();
    }

    /**
     * Updates the caption of a selected photo with an inputted caption.
     */
    public void updateCaption() {
        int index = photos.getSelectionModel().getSelectedIndex();
        if (index == -1) return;
        album.getPhotos().get(index).setCaption(caption.getText());
        obsList = FXCollections.observableArrayList(album.getPhotos());
        photos.setItems(obsList);
    }
    
    /**
     * Opens a new window for viewing a selected photo.
     * @throws IOException if photo-viewing issues occur
     */
    public void viewPhoto() throws IOException {
        int index = photos.getSelectionModel().getSelectedIndex();
        if (index == -1) return;
        Photo p = album.getPhotos().get(index);
        Utility.openedPhotos.add(p);
        Utility.currentAlbum = album;
        Stage photoStage = new Stage();
        Utility.photoStages.add(photoStage);
        Parent root = FXMLLoader.load(getClass().getResource("photo.fxml"));
        photoStage.setScene(new Scene(root, 1280, 720));
        photoStage.setResizable(false);
        photoStage.show();
    }

    /**
     * Copies a selected photo to a selected album.
     * @throws IOException if photo copying issues occur
     */
    public void copyPhoto() throws IOException {
        if (photos.getSelectionModel().getSelectedIndex() == -1) return;
        Utility.currentMode = Utility.TransferMode.COPY;
        transferPhoto();
    }

    /**
     * Moves a selected photo to a selected album.
     * @throws IOException if photo moving issues occur
     */
    public void movePhoto() throws IOException {
        if (photos.getSelectionModel().getSelectedIndex() == -1) return;
        Utility.currentMode = Utility.TransferMode.MOVE;
        transferPhoto();
    }

    /**
     * Performs actions for copying or moving a photo to another album.
     * @throws IOException if photo transferring issues occur
     */
    public void transferPhoto() throws IOException {
        if (Utility.currentUser.getAlbums().size() == 1) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No destination album exists.");
            alert.setContentText("You cannot transfer a photo without more than one album.");
            alert.showAndWait();
            return;
        }
        Stage transferStage = new Stage();
        Utility.currentAlbum = album;
        Utility.currentPhoto = album.getPhotos().get(photos.getSelectionModel().getSelectedIndex());
        Utility.currentAlbumController = this;
        Parent root = FXMLLoader.load(getClass().getResource("albumdestination.fxml"));
        transferStage.setScene(new Scene(root, 640, 320));
        String mode = "Move";
        if (Utility.currentMode == Utility.TransferMode.COPY) mode = "Copy";
        transferStage.setTitle(mode + " Photo");
        transferStage.setResizable(false);
        transferStage.show();
    }

    /**
     * Updates UI according to the state of the program.
     */
    public void update() {
        name.setText(album.getName());
        stage.setTitle(album.getName());
    }

    /**
     * Opens the photo search window.
     * @throws IOException if issues occur when opening the search window
     */
    public void openSearch() throws IOException {
        Utility.currentAlbum = album;
        Stage searchStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
        searchStage.setScene(new Scene(root, 1280, 720));
        searchStage.setTitle("Album Search");
        searchStage.setResizable(false);
        searchStage.show();
    }
}