package photos.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import photos.Album;
import photos.Photo;
import photos.Tag;
import photos.TagValue;
import photos.Utility;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Our controller class for the photo view scene.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class PhotoController {
    /**
     * The list of all tag values for this scene's photo.
     */
    @FXML ListView<TagValue> tags;

    /**
     * The caption of this scene's photo.
     */
    @FXML Text caption;

    /**
     * The date of this scene's photo.
     */
    @FXML Text date;

    /**
     * The object used to display this photo.
     */
    @FXML ImageView image;

    /**
     * The button used to view the previous photo in the album.
     */
    @FXML Button previous;

    /**
     * The button used to view the next photo in the album.
     */
    @FXML Button next;

    /**
     * A dropdown menu of potential tags for the user to add for this scene's photo.
     */
    @FXML ChoiceBox<Tag> tag;

    /**
     * The button used to add a selected tag to this scene's photo.
     */
    @FXML Button addTag;

    /**
     * The button used to delete a selected tag from this scene's photo.
     */
    @FXML Button deleteTag;

    /**
     * A field for the user to enter a value for a selected tag.
     */
    @FXML TextField tagValue;

    /**
     * The observable list for <code>tags</code>.
     */
    ObservableList<TagValue> obsList;

    /**
     * The observable list for all tags of this user.
     */
    ObservableList<Tag> globalList;

    /**
     * The album to which this scene's photo belongs.
     */
    Album album;

    /**
     * The photo of this scene.
     */
    Photo photo;

    /**
     * The stage of this scene.
     */
    Stage stage;
    
    /**
     * The method to initialize this scene.
     * @throws FileNotFoundException if issues occur when opening this scene's photo
     */
    public void initialize() throws FileNotFoundException {
        photo = Utility.openedPhotos.get(Utility.openedPhotos.size() - 1);
        stage = Utility.photoStages.get(Utility.photoStages.size() - 1);
        album = Utility.currentAlbum;
        Utility.openedPhotoWindows.add(this);
        stage.setOnCloseRequest(e -> clear());
        openImage();
        setTags();
        setGlobalTags();
        tags.getSelectionModel().selectedIndexProperty().addListener(e -> updateTags());
        tag.getSelectionModel().selectedIndexProperty().addListener(e -> updateGlobalTags());
        tagValue.textProperty().addListener(e -> updateTextFields());
        updateTags();
        updateGlobalTags();
        tags.getSelectionModel().clearAndSelect(-1);
        update();
    }

    /**
     * Updates the UI depending on how many photos exist in the album.
     */
    public void update() {
        if (album.getPhotos().size() > 1) {
            next.setDisable(false);
            previous.setDisable(false);
        } else {
            next.setDisable(true);
            previous.setDisable(true);
        }
    }

    /**
     * Updates the UI depending on the state of the program. Specialized for <code>addTag</code>.
     */
    public void updateTextFields() {
        addTag.setDisable(tagValue.getText().equals(""));
    }

    /**
     * Updates the UI depending on the state of the program. Specialized for <code>deleteTag</code>.
     */
    public void updateTags() {
        deleteTag.setDisable(tags.getSelectionModel().getSelectedIndex() == -1);
    }

    /**
     * Updates the list of all tags for this user.
     */
    public void updateGlobalTags() {
        ArrayList<Tag> ref = new ArrayList<Tag>(Utility.currentUser.tags);
        ref.remove(new Tag(false, "Date"));
        Tag t = ref.get(tag.getSelectionModel().getSelectedIndex());
        boolean disable = t.getName() == null;
        addTag.setDisable(disable);
        if (disable) { tagValue.setText(""); tagValue.setDisable(true); }
        else { tagValue.setDisable(false); }
        updateTextFields();
    }

    /**
     *  Closes this stage.
     */
    public void back() {
        Utility.openedPhotos.remove(photo);
        Utility.photoStages.remove(stage);
        stage.close();
    }

    /**
     * Removes this scene from <code>Utility.openedPhotoWindows</code>.
     */
    public void clear() {
        Utility.openedPhotoWindows.remove(this);
        back();
    }

    /**
     * Sets the tags for this user on initialization.
     */
    public void setGlobalTags() {
        globalList = FXCollections.observableArrayList(Utility.currentUser.tags);
        globalList.remove(new Tag(false, "Date"));
        tag.setItems(globalList);
        tag.getSelectionModel().clearAndSelect(0);
    }

    private void openImage() throws FileNotFoundException {
        int ind = Utility.users.indexOf(Utility.currentUser);
        int photoInd = Utility.photos.get(ind).indexOf(photo);
        String directory = Utility.directories.get(ind).get(photoInd);
        if (directory.substring(0, 1).equals(File.separator)) directory = Utility.workingDirectory + directory;
        File f = Utility.files.get(ind).get(photoInd);
        InputStream stream = new FileInputStream(directory);
        Image img = new Image(stream);
        image.setImage(img);
        image.setPreserveRatio(true);
        long epoch = f.lastModified();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(epoch);
        cal.set(Calendar.MILLISECOND,0);
        date.setText(cal.getTime().toString());
        caption.setText(photo.getCaption());
        stage.setTitle(photo.getName());
        setTags();
    }

    private void setTags() {
        obsList = FXCollections.observableArrayList(photo.getTagValues());
        tags.setItems(obsList);
        if (obsList.size() != -1) tags.getSelectionModel().clearAndSelect(obsList.size()-1);
    }

    /**
     * Adds a selected tag to this photo.
     */
    public void addTag() {
        ArrayList<Tag> tags = photo.getTags();
        Tag selectedTag = tag.getSelectionModel().getSelectedItem();
        String value = tagValue.getText();
        Tag tag;
        if (tags.contains(selectedTag)) {
            tag = tags.get(tags.indexOf(selectedTag));
            for (TagValue tagValue : tag.values) {
                if (tagValue.value.equals(value)) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Tag value already exists.");
                    alert.setContentText("Please choose a new value for this tag.");
                    alert.showAndWait();
                    return;
                }
            }
        } else {
            tag = new Tag(selectedTag.singular, selectedTag.getName());
            tags.add(tag);
        }
        
        if (tag.add(value)) {
            tagValue.setText("");
            setTags();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Tag type is singular.");
            alert.setContentText("You cannot add multiple tag values to a singular tag.");
            alert.showAndWait();
        }
    }

    /**
     * Deletes a selected tag from this photo.
     */
    public void deleteTag() {
        TagValue tv = tags.getSelectionModel().getSelectedItem();
        Tag t = tv.parent;
        t.remove(tv.value);
        if (t.values.size() == 0) { photo.getTags().remove(t); }
        setTags();
    }
    
    /**
     * Views the next photo in the album.
     * @throws FileNotFoundException if issues occur when fetching the next photo
     */
    public void next() throws FileNotFoundException {
        ArrayList<Photo> photos = album.getPhotos();
        if (photos.size() == 1) return;
        int ind = photos.indexOf(photo);
        if (ind + 1 >= photos.size()) {
            ind = 0;
        }
        else { ++ind; }
        Utility.openedPhotos.remove(photo);
        photo = photos.get(ind);
        Utility.openedPhotos.add(photo);
        openImage();
    }

    /**
     * Views the previous photo in the album.
     * @throws FileNotFoundException if issues occur when fetching the previous photo
     */
    public void prev() throws FileNotFoundException {
        ArrayList<Photo> photos = album.getPhotos();
        if (photos.size() == 1) return;
        int ind = photos.indexOf(photo);
        if (ind - 1 < 0) {
            ind = photos.size()-1;
        }
        else { --ind; }
        Utility.openedPhotos.remove(photo);
        photo = photos.get(ind);
        Utility.openedPhotos.add(photo);
        openImage();
    }

    /**
     * Closes this window.
     */
    public void close() {
        Stage stage = (Stage) tag.getScene().getWindow();
        stage.close();
    }
}