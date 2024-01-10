package photos.controllers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import photos.Album;
import photos.Photo;
import photos.Photos;
import photos.Utility;

/**
 * Our controller class for the user.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class UserController {
    /**
     * The field for the user to input an album name to add.
     */
    @FXML TextField album;

    /**
     * The button that adds a new album to the user account.
     */
    @FXML Button add;

    /**
     * The button that renames a selected album.
     */
    @FXML Button rename;

    /**
     * The button that opens a selected album.
     */
    @FXML Button open;

    /**
     * The button that deletes a selected album.
     */
    @FXML Button delete;

    /**
     * The list of all albums of the user.
     */
    @FXML ListView<String> list;

    /**
     * The button to perform a logout and return to the login page.
     */
    @FXML Button logout;

    /**
     * The button to open the tag manager.
     */
    @FXML Button tag;

    /**
     * Welcome text for the user. Changes depending on username.
     */
    @FXML Text welcome;

    private ObservableList<String> obsList;

    /**
     * The method to initialize this scene.
     */
    public void initialize() {
        Utility.currentUserStage.setOnCloseRequest(e -> {
            try {
                logout();
            } catch (Exception event) {}
        });
        list.getSelectionModel().selectedIndexProperty().addListener(e -> load());
        welcome.setText("Welcome,\n" + Utility.currentUser.toString());
        update();
        Utility.currentUserController = this;
        list.getSelectionModel().selectedIndexProperty().addListener(e -> updateUI());
        updateUI();
    }
    
    /**
     * Updates <code>list</code> with appropriate albums.
     */
    public void update() {
        obsList = FXCollections.observableList(Utility.currentUser.getAlbumNames());
        list.setItems(obsList);
    }

    /**
     * Adds a new album to the user account.
     */
    public void add() {
        if (Utility.currentUser.albumExistsByName(album.getText())) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Album already exists.");
            alert.setContentText("You cannot add an existing album. Please add a new album.");
            alert.showAndWait();
        } else {
            Album a = new Album(album.getText());
            Utility.currentUser.addAlbum(a);
            obsList.add(album.getText() + " | 0 photos");
            list.setItems(obsList);
            list.getSelectionModel().clearAndSelect(obsList.size()-1);
        }
    }

    /**
     * Updates the UI according to the state of the program.
     */
    public void updateUI() {
        boolean enable = list.getSelectionModel().getSelectedIndex() == -1;
        open.setDisable(enable);
        rename.setDisable(enable);
        delete.setDisable(enable);
    }

    /**
     * Renames a selected album.
     */
    public void rename() {
        if (list.getSelectionModel().getSelectedIndex() == -1) return;
        if (Utility.currentUser.albumExistsByName(album.getText())) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Album already exists.");
            alert.setContentText("Please choose a unique album name.");
            alert.showAndWait();
        } else {
            String albumName = list.getSelectionModel().getSelectedItem();
            int albumIndex = list.getSelectionModel().getSelectedIndex();
            Album current = Utility.currentUser.getAlbums().get(albumIndex);
            String text = album.getText();
            Utility.currentUser.renameAlbum(current, text);
            obsList.remove(albumName);
            obsList.add(albumIndex, text + " | " + current.getPhotos().size() + " photos" + sortDates(current));
            list.setItems(obsList);
            if (Utility.openedAlbums.contains(current)) Utility.openedAlbumWindows.get(Utility.openedAlbums.indexOf(current)).update();
            list.getSelectionModel().clearAndSelect(albumIndex);
        }
    }

    /**
     * Deletes a selected album.
     */
    public void delete() {
        int albumIndex = list.getSelectionModel().getSelectedIndex();
        if (albumIndex == -1) return;
        if (Utility.openedAlbums.contains(Utility.currentUser.getAlbums().get(albumIndex))) {
            int globalAlbumIndex = Utility.openedAlbums.indexOf(Utility.currentUser.getAlbums().get(albumIndex));
            Utility.openedAlbumWindows.get(globalAlbumIndex).back();
            Utility.openedAlbumWindows.remove(albumIndex);
        }
        Utility.currentUser.deleteAlbum(Utility.currentUser.getAlbums().get(albumIndex));
        obsList.remove(albumIndex);
        list.setItems(obsList);
    }

    /**
     * Opens a selected album.
     * @throws Exception if issues occur when opening an album
     */
    public void open() throws Exception {
        if (list.getSelectionModel().getSelectedIndex() == -1 || Utility.openedAlbums.contains(Utility.currentUser.getAlbums().get(list.getSelectionModel().getSelectedIndex()))) return;
        int index = list.getSelectionModel().getSelectedIndex();
        Utility.openedAlbums.add(Utility.currentUser.getAlbums().get(list.getSelectionModel().getSelectedIndex()));
        Stage albumStage = new Stage();
        Utility.albumStages.add(albumStage);
        Parent root = FXMLLoader.load(getClass().getResource("album.fxml"));
        albumStage.setScene(new Scene(root, 1280, 720));
        Album a = Utility.currentUser.getAlbums().get(index);
        albumStage.setTitle(a.getName());
        albumStage.setResizable(false);
        albumStage.show();
    }

    /**
     * Updates <code>album</code> to the name of selected album.
     */
    public void load() {
        int n = list.getSelectionModel().getSelectedIndex();
        String text = "";
        if (n == -1) text = "";
        else { text = Utility.currentUser.getAlbums().get(n).getName(); }
        album.setText(text);
    }

    /**
     * Performs a logout. Returns to login page and writes app data to data file.
     * @throws Exception if issues occur when switching scenes.
     */
    public void logout() throws Exception {
        Stage primaryStage = (Stage) logout.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setTitle("Login");
        for (AlbumController ac : Utility.openedAlbumWindows) ac.back();
        Utility.openedAlbumWindows.clear();
        for (AlbumDestinationController dc : Utility.openedDestinationWindows) dc.close();
        Utility.openedDestinationWindows.clear();
        for (SearchController sc : Utility.openedSearchWindows) sc.cancel();
        Utility.openedSearchWindows.clear();
        for (TagsController tc : Utility.openedTagsWindows) tc.close();
        Utility.openedTagsWindows.clear();
        for (PhotoController pc : Utility.openedPhotoWindows) pc.close();
        Utility.openedTagsWindows.clear();
        Utility.currentUser = null;
        Utility.currentUserController = null;
        Utility.currentAlbumController = null;
        Photos.writeApp();
    }

    /**
     * Opens the tag manager window.
     * @throws IOException if issues occur when opening tag manager
     */
    public void openTags() throws IOException {
        Stage tagStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("tags.fxml"));
        tagStage.setScene(new Scene(root, 720, 376));
        tagStage.setTitle("Tag Manager");
        tagStage.setResizable(false);
        tagStage.show();
    }

    /**
     * Sorts the dates of all photos in an album.
     * @param a the album whose photos' dates are to be sorted
     * @return a string containing information about the earliest and latest dates in album <code>a</code>
     */
    public static String sortDates(Album a) {
        if (a.getPhotos().size() < 1) return "";
        ArrayList<Calendar> dates = new ArrayList<Calendar>();
        for (Photo p : a.getPhotos()) {
            File f = Utility.files.get(Utility.users.indexOf(Utility.currentUser)).get(Utility.photos.get(Utility.users.indexOf(Utility.currentUser)).indexOf(p));
            long epoch = f.lastModified();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(epoch);
            cal.set(Calendar.MILLISECOND,0);
            dates.add(cal);
        }
        Collections.sort(dates);
        Calendar startDate = dates.get(0);
        Calendar endDate = dates.get(dates.size()-1);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String start = f.format(startDate.getTime());
        String end = f.format(endDate.getTime());
        return " | " + start + " to " + end;
    }
}