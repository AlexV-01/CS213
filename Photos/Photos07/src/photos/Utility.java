package photos;

import java.io.File;
import java.util.*;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import photos.controllers.AlbumController;
import photos.controllers.AlbumDestinationController;
import photos.controllers.PhotoController;
import photos.controllers.SearchController;
import photos.controllers.TagsController;
import photos.controllers.UserController;

/**
 * A utility class that contains information universal to the program.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class Utility {
    /**
     * A string that contains the working directory.
     */
    public static String workingDirectory;

    /**
     * An enumeration of different states of photo transfer.
     */
    public enum TransferMode { COPY, MOVE }

    /**
     * An ArrayList of type <code>AlbumController</code> that contains all opened album windows.
     */
    public static ArrayList<AlbumController> openedAlbumWindows = new ArrayList<AlbumController>();

    /**
     * An ArrayList of type <code>PhotoController</code> that contains all opened photo windows.
     */
    public static ArrayList<PhotoController> openedPhotoWindows = new ArrayList<PhotoController>();

    /**
     * An ArrayList of type <code>AlbumDestinationController</code> that contains all opened photo transfer.
     */
    public static ArrayList<AlbumDestinationController> openedDestinationWindows = new ArrayList<AlbumDestinationController>();

    /**
     * An ArrayList of type <code>SearchController</code> that contains all opened search windows.
     */
    public static ArrayList<SearchController> openedSearchWindows = new ArrayList<SearchController>();

    /**
     * An ArrayList of type <code>TagsController</code> that contains all opened tag windows.
     */
    public static ArrayList<TagsController> openedTagsWindows = new ArrayList<TagsController>();

    /**
     * An ArrayList of type <code>Stage</code> that contains all album stages.
     */
    public static ArrayList<Stage> albumStages = new ArrayList<Stage>();

    /**
     * An ArrayList of type <code>Stage</code> that contains all photo stages.
     */
    public static ArrayList<Stage> photoStages = new ArrayList<Stage>();

    /**
     * An ArrayList of type <code>Album</code> that contains all opened albums.
     */
    public static ArrayList<Album> openedAlbums = new ArrayList<Album>();

    /**
     * An ArrayList of type <code>Photo</code> that contains all opened photos.
     */
    public static ArrayList<Photo> openedPhotos = new ArrayList<Photo>();

    /**
     * An ArrayList of type <code>String</code> that contains all usernames.
     */
    public static ArrayList<String> userNames = new ArrayList<String>();

    /**
     * An ArrayList of type <code>User</code> that contains all users.
     */
    public static ArrayList<User> users = new ArrayList<User>();

    /**
     * The current user that is logged in.
     */
    public static User currentUser;

    /**
     * The stage object of the current user.
     */
    public static Stage currentUserStage;

    /**
     * The stage of the admin account.
     */
    public static Stage adminStage;

    /**
     * The current album that is open.
     */
    public static Album currentAlbum;

    /**
     * An ArrayList of ArrayLists of photos. Each nested ArrayList represents a different user.
     */
    public static ArrayList<ArrayList<Photo>> photos = new ArrayList<ArrayList<Photo>>();

    /**
     * An ArrayList of ArrayLists of files. Each nested ArrayList represents a different user.
     */
    public static ArrayList<ArrayList<File>> files = new ArrayList<ArrayList<File>>();

    /**
     * An ArrayList of ArrayLists of the number of instances of each photo currently in use for a user.
     * Each nested ArrayList represents a different user.
     */
    public static ArrayList<ArrayList<Integer>> useCount = new ArrayList<ArrayList<Integer>>();

    /**
     * An ArrayList of ArrayLists of directories. Each nested ArrayList represents a different user.
     */
    public static ArrayList<ArrayList<String>> directories = new ArrayList<ArrayList<String>>();

    /**
     * The current status of photo transfer.
     */
    public static TransferMode currentMode;

    /**
     * The current photo being viewed.
     */
    public static Photo currentPhoto;

    /**
     * The controller of the current album.
     */
    public static AlbumController currentAlbumController;

    /**
     * The controller of the current user.
     */
    public static UserController currentUserController;

    /**
     * A private constructor meant to prevent instantiation of this class.
     */
    private Utility() {
    }

    /**
     * Gets a user by username.
     * @param n a string representing the desired user's username
     * @return the desired <code>User</code>
     */
    public static User getUserByName(String n) {
        for (User u : users) {
            if (u.toString().equals(n)) return u;
        }
        return null;
    }

    /**
     * Initializes this class when the program starts. Performs necessary operations for the program to run.
     */
    public static void initialize() {
        workingDirectory = System.getProperty("user.dir");
        ArrayList<ArrayList<String>> dirCopy = new ArrayList<ArrayList<String>>(directories);
        for (int i = 0; i < dirCopy.size(); i++) {
            files.add(new ArrayList<File>());
            for (int j = 0; j < dirCopy.get(i).size(); j++) {
                String dir = dirCopy.get(i).get(j);
                String fileDir = dir;
                if (dir.substring(0, 1).equals(File.separator)) fileDir = workingDirectory + dir;
                try {
                    File f = new File(fileDir);
                    new Image(fileDir);
                    files.get(i).add(f);
                }
                catch (Exception e) {
                    int ind = directories.get(i).indexOf(dir);
                    directories.get(i).remove(ind);
                    useCount.get(i).remove(ind);
                    Photo p = photos.get(i).get(j);
                    for (Album a : p.albums) {
                        a.removePhoto(p);
                    }
                    photos.get(i).remove(ind);
                }
            }
        }
    }
}