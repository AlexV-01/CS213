package photos;

import java.io.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Our main class. Runs the program.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class Photos extends Application {
    /**
     * Stores the directory of the info file.
     */
    public static final String storeDir = "./Photos07/data";

    /**
     * Stores the name of the info file.
     */
    public static final String storeFile = "info.dat";

    /**
     * Starts the application.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        readApp();
        Utility.initialize();
        Parent root = FXMLLoader.load(getClass().getResource("controllers/login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    /**
     * Our main method. It launches the app.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Reads and loads saved data from info file.
     * @throws IOException if input issues occur
     * @throws ClassNotFoundException if input issues occur
     */
    public static void readApp() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
        InfoLoader il = (InfoLoader) ois.readObject();
        il.loadToUtility();
        ois.close();
    }

    /**
     * Writes current data to info file.
     * @throws IOException if output issues occur
     */
    public static void writeApp() throws IOException {
        InfoLoader il = new InfoLoader();
        il.loadFromUtility();
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
        oos.writeObject(il);
        oos.close();
    }
}