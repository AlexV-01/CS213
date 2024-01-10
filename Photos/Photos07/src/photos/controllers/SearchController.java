package photos.controllers;

import java.util.ArrayList;
import java.util.Calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import photos.Album;
import photos.Photo;
import photos.Tag;
import photos.TagValue;
import photos.Utility;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Our controller class for the photo search window.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class SearchController {
    /**
     * A menu to select the search type of the first field.
     */
    @FXML ChoiceBox<Tag> fieldOne;

    /**
     * A menu to select the search type of the second field.
     */
    @FXML ChoiceBox<Tag> fieldTwo;

    /**
     * The button used to perform the search.
     */
    @FXML Button search;

    /**
     * The button used to close out of the search window.
     */
    @FXML Button cancel;

    /**
     * The button used to create an album based on the photos that are returned by the search.
     */
    @FXML Button create;

    /**
     * A list of all photos returned by the search.
     */
    @FXML ListView<Photo> photos;

    /**
     * The observable list of tags for this search.
     */
    ObservableList<Tag> fieldList;

    /**
     * The observable list of photos that result from this search.
     */
    ObservableList<Photo> photosList;

    /**
     * A checkbox determining whether the search should be conjunctive or disjunctive.
     */
    @FXML CheckBox conjunctive;

    /**
     * A field for the value of the first selected tag.
     */
    @FXML TextField one;

    /**
     * A field for the value of the second selected tag.
     */
    @FXML TextField two;

    /**
     * A field for the name of a potential new album to be generated from search results.
     */
    @FXML TextField newAlbum;

    /**
     * The start of the date range for a date-based search.
     */
    @FXML DatePicker start;

    /**
     * The end of the date ranged for a date-based search.
     */
    @FXML DatePicker end;

    /**
     * A prompt for the value of the first tag. Depends on selected search criterion.
     */
    @FXML Text value1Text;

    /**
     * A prompt for the value of the second tag. Depends on selected search criterion.
     */
    @FXML Text value2Text;

    /**
     * The state of the search criteria. Depends on which or how many criteria are selected.
     */
    StateMode currentState;

    /**
     * The current album.
     */
    Album album;

    private ArrayList<Photo> newPhotos = new ArrayList<Photo>();
    private enum StateMode { DATE, SINGLE, DOUBLE }

    /**
     * The method to initialize this scene.
     */
    public void initialize() {
        album = Utility.currentAlbum;
        update();
        fieldOne.getSelectionModel().clearAndSelect(0);
        fieldTwo.getSelectionModel().clearAndSelect(0);
        fieldOne.getSelectionModel().selectedIndexProperty().addListener(e -> updateUI());
        fieldTwo.getSelectionModel().selectedIndexProperty().addListener(e -> updateUI());
        updateUI();
        final StringConverter<LocalDate> startConverter = start.getConverter();
            start.setConverter(new StringConverter<LocalDate>() {
                @Override public String toString(LocalDate value) {
                    return startConverter.toString(value);
                }

                @Override public LocalDate fromString(String text) {
                    try {
                        return startConverter.fromString(text);
                    } catch (DateTimeParseException ex) {
                        return null;
                    }
                }
            });
        final StringConverter<LocalDate> endConverter = end.getConverter();
            end.setConverter(new StringConverter<LocalDate>() {
                @Override public String toString(LocalDate value) {
                    return endConverter.toString(value);
                }

                @Override public LocalDate fromString(String text) {
                    try {
                        return endConverter.fromString(text);
                    } catch (DateTimeParseException ex) {
                        return null;
                    }
                }
            });
        one.textProperty().addListener(e -> updateTags());
        two.textProperty().addListener(e -> updateTags());
        start.focusedProperty().addListener(e -> updateDate());
        end.focusedProperty().addListener(e -> updateDate());
        create.setDisable(true);
        newAlbum.setDisable(true);

        Utility.openedSearchWindows.add(this);
    }

    /**
     * Updates <code>fieldList</code> according to the state of the program.
     */
    public void update() {
        fieldList = FXCollections.observableArrayList(Utility.currentUser.tags);
        fieldOne.setItems(fieldList);
        fieldTwo.setItems(fieldList);
    }

    /**
     * Updates the UI according to the state of the program.
     */
    public void updateUI() {
        String n1 = Utility.currentUser.tags.get(fieldOne.getSelectionModel().getSelectedIndex()).getName();
        String n2 = Utility.currentUser.tags.get(fieldTwo.getSelectionModel().getSelectedIndex()).getName();
        // DATE STATE
        if ("Date".equals(n1) || "Date".equals(n2)) {
            one.setVisible(false);
            two.setVisible(false);
            one.setText("");
            two.setText("");
            start.setVisible(true);
            end.setVisible(true);
            conjunctive.setDisable(true);
            conjunctive.setSelected(false);
            value1Text.setText("Start:");
            value2Text.setText("End:");
            if ("Date".equals(n1)) { fieldTwo.setDisable(true); }
            else { fieldOne.setDisable(true); }
            currentState = StateMode.DATE;
            updateDate();
        }
        // OTHER STATES (SHARED PROPERTIES)
        else {
            one.setVisible(true);
            two.setVisible(true);
            start.setVisible(false);
            start.setValue(null);
            end.setVisible(false);
            end.setValue(null);
            value1Text.setText("Value 1:");
            value2Text.setText("Value 2:");
            fieldOne.setDisable(false);
            fieldTwo.setDisable(false);
            // NONE STATE
            if (n1 == null || n2 == null) {
                conjunctive.setDisable(true);
                conjunctive.setSelected(false);
                if (n1 == null) {
                    one.setDisable(true);
                    one.setText("");
                }
                else { one.setDisable(false); }
                if (n2 == null) {
                    two.setDisable(true);
                    two.setText("");
                }
                else { two.setDisable(false); }
                currentState = StateMode.SINGLE;
            }
            // NORMAL STATE
            else {
                one.setDisable(false);
                two.setDisable(false);
                if (n1 == n2 && Utility.currentUser.tags.get(fieldOne.getSelectionModel().getSelectedIndex()).singular) {
                    conjunctive.setDisable(true);
                    conjunctive.setSelected(false);
                }
                else { conjunctive.setDisable(false); }
                currentState = StateMode.DOUBLE;
            }
            updateTags();
        }
    }
    
    /**
     * Conducts a search of photos that satisfy provided search criteria.
     */
    public void searchPhotos() {
        newPhotos = new ArrayList<Photo>();
        if (currentState != StateMode.DATE) {
            Tag one = fieldOne.getSelectionModel().getSelectedItem();
            Tag two = fieldTwo.getSelectionModel().getSelectedItem();
            newPhotos = new ArrayList<Photo>();

            if (currentState == StateMode.DOUBLE) {
                boolean conj = conjunctive.isSelected();
                TagValue tv1 = new TagValue(this.one.getText(), one);
                TagValue tv2 = new TagValue(this.two.getText(), two);
                for (Photo p : album.getPhotos()) {
                    if (conj) {
                        if (p.getTagValues().contains(tv1) && p.getTagValues().contains(tv2)) {
                            newPhotos.add(p);
                        }
                    } else {
                        if (p.getTagValues().contains(tv1) || p.getTagValues().contains(tv2)) {
                            newPhotos.add(p);
                        }
                    }
                }
            } else {
                TagValue tv;
                if (one.getName() != null) { tv = new TagValue(this.one.getText(), one); } else { tv = new TagValue(this.two.getText(), two); }
                for (Photo p : album.getPhotos()) {
                    if (p.getTagValues().contains(tv)) newPhotos.add(p);
                }
            }
        } else {
            LocalDate sd = start.getValue();
            LocalDate ed = end.getValue();
            int ind = Utility.users.indexOf(Utility.currentUser);
    
            for (Photo p : album.getPhotos()) {
                int photoInd = Utility.photos.get(ind).indexOf(p);
                File f = Utility.files.get(ind).get(photoInd);
                long epoch = f.lastModified();
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(epoch);
                cal.set(Calendar.MILLISECOND,0);
                LocalDate ld = cal.toInstant().atZone(cal.getTimeZone().toZoneId()).toLocalDate();
                if (ld.compareTo(sd) >= 0 && ld.compareTo(ed) <= 0) newPhotos.add(p);
            }
        }

        photosList = FXCollections.observableArrayList(newPhotos);
        photos.setItems(photosList);
        if (newPhotos.size() == 0) { create.setDisable(true); newAlbum.setText(""); newAlbum.setDisable(true); } else { create.setDisable(false); newAlbum.setDisable(false); }
    }

    /**
     * Creates an album containing the photos returned from the search.
     */
    public void createAlbum() {
        if (Utility.currentUser.albumExistsByName(newAlbum.getText())) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Album already exists.");
            alert.setContentText("You cannot add an existing album. Please add a new album.");
            alert.showAndWait();
        } else {
            Album a = new Album(newAlbum.getText(), newPhotos);
            Utility.currentUser.addAlbum(a);
            Utility.currentUserController.update();
            newAlbum.setText("");

        }
    }

    /**
     * Updates the status of the search button depending on date criteria.
     */
    public void updateDate() {
        if ((start.getValue() == null || end.getValue() == null) || (start.getValue().compareTo(end.getValue()) > 0)) {
            search.setDisable(true);
            if (start.getValue() == null) start.getEditor().clear();
            if (end.getValue() == null) end.getEditor().clear();
            return;
        }

        search.setDisable(false);
    }

    /**
     * Updates status of the search button depending on tag criteria.
     */
    public void updateTags() {
        if (currentState == StateMode.DOUBLE && (one.getText() == "" || two.getText() == "") || (one.getText() == "" && two.getText() == "")) { search.setDisable(true); return; }
        search.setDisable(false);
    }

    /**
     * Cancels the search operation and closes the search stage.
     */
    public void cancel() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}