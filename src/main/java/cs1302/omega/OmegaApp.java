package cs1302.omega;

import cs1302.api.Tools;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.io.InputStreamReader;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import java.net.URLEncoder;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.scene.text.TextAlignment;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.io.ObjectInputFilter.Config;

import static cs1302.api.Tools.get;
import static cs1302.api.Tools.getJson;
import static cs1302.api.Tools.UTF8;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class OmegaApp extends Application {

    private Image[] storage;
    private URL url;
    private InputStreamReader reader;
    private JsonElement je;
    private JsonObject root;
    private JsonArray results;
    private int numResults;
    private String sUrl;
    private String encoded;
    private JsonObject result;
    private JsonElement artwork;
    private String link;
    private String dogApi;
    private String newLink;
    private String catLink;
    private Properties config;
    private static final int DEF_HEIGHT = 100;
    private static final int DEF_WIDTH = 100;


    /**
     * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public OmegaApp() {}

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {



        VBox pane = new VBox();
        pane.setSpacing(40);

        dogs();
        cats();
        Text txt = new Text("Click the button to prove you are not a robot.");
        Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12);
        txt.setFont(font);
        txt.setWrappingWidth(565);
        txt.setTextAlignment(TextAlignment.CENTER);
        Button button = new Button("Press Me!");

        Image picture = new Image(newLink);
        ImageView img = new ImageView(picture);


        //  pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(txt, button);
        pane.getChildren().add(img);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(15));
        Scene scene = new Scene(new Group(pane), Color.BEIGE);



        stage.setMaxWidth(1280);
        stage.setMaxHeight(720);
        stage.setTitle("OmegaApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.show();
        Platform.runLater(() -> {
            stage.sizeToScene();
        });
    } // start




    /**
     * JSON method to store all of the cat images in the query.
     * @param input the query to be searched.
     */
    public void cats() {

        String configPath = "resources/config.properties";
        try (FileInputStream configFileStream = new FileInputStream(configPath)) {
            config = new Properties();
            config.load(configFileStream);

        } catch (IOException ioe) {
            System.err.println(ioe);
            ioe.printStackTrace();
        } // try


        final String catApi = config.getProperty("thecatapi.apikey");
        String endpoint = "https://api.thecatapi.com/v1";
        String method = "/breeds";
        String url = (endpoint + method + "?api_key=" + dogApi);
        try {
            JsonElement root = getJson(url);

            catLink = get(root, 1, "image", "url").getAsString();

            System.out.println(newLink);

        } catch (IOException ioe) {
            System.out.println(ioe);
            System.out.println("cats exception");
        } // try
    }





    /**
     * JSON method to store all of the dog images in the query.
     * @param input the query to be searched.
     */
    public void dogs() {

        String configPath = "resources/config.properties";
        try (FileInputStream configFileStream = new FileInputStream(configPath)) {
            config = new Properties();
            config.load(configFileStream);

        } catch (IOException ioe) {
            System.err.println(ioe);
            ioe.printStackTrace();
        } // try


        final String dogApi = config.getProperty("thedogapi.apikey");
        String endpoint = "https://api.thedogapi.com/v1";
        String method = "/breeds";
        String url = (endpoint + method + "?api_key=" + dogApi);
        try {
            JsonElement root = getJson(url);

            newLink = get(root, 1, "image", "url").getAsString();



        } catch (IOException ioe) {
            System.out.println(ioe);
            System.out.println("dogs exception");
        } // try
    }

} // OmegaApp
