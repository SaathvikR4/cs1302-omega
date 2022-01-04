package cs1302.omega;

import javafx.scene.input.MouseEvent;
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
import java.io.FileNotFoundException;
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
import javafx.scene.effect.ColorAdjust;

import static cs1302.api.Tools.get;
import static cs1302.api.Tools.getJson;
import static cs1302.api.Tools.UTF8;
import javafx.scene.layout.Priority;

/**
 * This app is meant to imitate a Prove you are not a robot
 * Capctha Application. The application has three rounds for you to
 * prove that you are indeed a human. In order to pass the Capctha the
 * user must select at least 5 dogs within the board and 0 cats. If the
 * requirments are met then the application will present a congratulation
 * screen or otherwise a failure screen which both present the option to
 * retry the Captcha.
 *
 * Dr. Cotterell approved this application on the bases that the images
 * downloaded from the two queries were changed after every round.
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
    private String dogLink;
    private String catLink;
    private Properties config;
    private static final int DEF_HEIGHT = 100;
    private static final int DEF_WIDTH = 100;
    private String[] fullArray = new String[60];
    private String[] catArray = new String[30];
    private String[] dogArray = new String[30];
    private GridPane grid;
    private Button submit = new Button("Next");
    private VBox newPane;
    private ImageView[] images;
    private int dogCount;
    private int catCount;
    private int round = 1;
    private Text newText;
    private Stage newWindow;
    private Button close = new Button("Close");
    private Stage congratsWindow;
    private Stage failWindow;

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
        mergeArrays();

        Text txt = new Text("Welcome to Saathvik Rukkannagari's Prove you're not a Robot Captcha!");
        Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16);
        txt.setFont(font);
        txt.setWrappingWidth(565);
        txt.setTextAlignment(TextAlignment.CENTER);

        Text txt2 = new Text("There will be a total of three tries, so try not to mess up!");
        Font font2 = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12);
        txt.setFont(font);
        txt.setWrappingWidth(565);
        txt.setTextAlignment(TextAlignment.CENTER);


        Text txt3 = new Text("Click on the button to prove you are not a robot.");
        Font font3 = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12);
        txt.setFont(font);
        txt.setWrappingWidth(565);
        txt.setTextAlignment(TextAlignment.CENTER);

        Button button = new Button("Press Me!");
        pane.getChildren().addAll(txt, txt2, txt3, button);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(15));
        Scene scene = new Scene(new Group(pane), Color.BEIGE);


        button.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {

                    round = 1;
                    catCount = 0;
                    dogCount = 0;
                    int z = 0;
                    int y = 0;
                    String object;
                    images = new ImageView[20];
                    grid = new GridPane();
                    grid.setHgap(5);
                    grid.setVgap(5);
                    for (int x = 0; x < 12; x++) {
                        object = fullArray[(int)Math.floor(Math.random() * 49)];

                        Image picture = new Image(object, DEF_WIDTH, DEF_HEIGHT, false, false);
                        images[x] = new ImageView(picture);
                        images[x].setPickOnBounds(true);
                        images[x].setOnMouseClicked(newImageHandler(images[x]));


                        grid.add(images[x], y, z);

                        y++;
                        if (y == 4) {
                            z++;
                            y = 0;
                        }
                    }
                    newText = new Text("Round " + round + " : Click on the dogs" +
                        " to prove you are not a robot.");
                    Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12);
                    newText.setFont(font);
                    newText.setTextAlignment(TextAlignment.CENTER);

                    newPane = new VBox();
                    newPane.setAlignment(Pos.CENTER);

                    newPane.getChildren().addAll(newText, grid, submit);
                    Scene secondScene = new Scene(newPane);

                    newWindow = new Stage();
                    newWindow.setMaxWidth(1280);
                    newWindow.setMaxHeight(720);
                    newWindow.setTitle("Prove You Are Not A Robot!");
                    newWindow.setScene(secondScene);

                    newWindow.show();
                    stage.close();
                    failWindow.close();
                    congratsWindow.close();
                }
            });


        close.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    System.exit(0);

                }
            });

        submit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    if ((dogCount > 4) && catCount == 0) {

                        VBox congrats = new VBox();
                        Text finalTxt = new Text("You have proved you are not a robot!");
                        Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12);
                        finalTxt.setFont(font);
                        finalTxt.setTextAlignment(TextAlignment.CENTER);

                        Text finalTxt2 = new Text("If you want to prove it again press Try Again.");
                        finalTxt2.setFont(font);
                        finalTxt2.setTextAlignment(TextAlignment.CENTER);


                        Image image = new Image("file:resources/success.jpg");

                        ImageView imgView = new ImageView(image);
                        button.setText("Try Again");

                        HBox closing = new HBox(10);
                        closing.setAlignment(Pos.CENTER);
                        closing.getChildren().addAll(close, button);

                        congrats.getChildren().addAll(finalTxt, finalTxt2, imgView, closing);

                        congrats.setAlignment(Pos.CENTER);
                        Scene congratsScene = new Scene(congrats);

                        congratsWindow = new Stage();
                        congratsWindow.setMaxWidth(1280);
                        congratsWindow.setMaxHeight(720);
                        congratsWindow.setTitle("Congratulations!");
                        congratsWindow.setScene(congratsScene);

                        congratsWindow.show();
                        newWindow.close();


                    } else {

                        if (round == 3) {
                            VBox fail = new VBox();
                            Text failTxt = new Text("It has been determined you are a robot.");
                            Font failFont = Font.font("verd" +
                                "ana", FontWeight.BOLD, FontPosture.REGULAR, 12);
                            failTxt.setFont(failFont);
                            failTxt.setTextAlignment(TextAlignment.CENTER);

                            Text retry = new Text("If you want to retry press Change My Mind.");
                            retry.setFont(failFont);
                            retry.setTextAlignment(TextAlignment.CENTER);


                            Image failure = new Image("file:resources" +
                                "/robot.jpg", 400, 400, false, false);
                            ImageView failImg = new ImageView(failure);

                            button.setText("Change My Mind");
                            HBox buttons = new HBox(10);
                            buttons.getChildren().addAll(close, button);
                            buttons.setAlignment(Pos.CENTER);

                            fail.getChildren().addAll(failTxt, retry, failImg, buttons);
                            fail.setAlignment(Pos.CENTER);
                            Scene failScene = new Scene(fail);
                            failWindow = new Stage();
                            failWindow.setMaxWidth(1280);
                            failWindow.setMaxHeight(720);
                            failWindow.setTitle("Failure");
                            failWindow.setScene(failScene);

                            failWindow.show();
                            newWindow.close();

                        } else {

                            round++;
                            dogCount = 0;
                            catCount = 0;
                            int z = 0;
                            int y = 0;
                            String object;

                            newText.setText("Round " + round + ": " +
                                "Click on the dogs to prove you are not a robot.");
                            for (int x = 0; x < 12; x++) {
                                object = fullArray[(int)Math.floor(Math.random() * 49)];

                                Image picture = new Image(object,
                                    DEF_WIDTH, DEF_HEIGHT, false, false);
                                images[x] = new ImageView(picture);
                                images[x].setPickOnBounds(true);
                                images[x].setOnMouseClicked(newImageHandler(images[x]));

                                grid.add(images[x], y, z);

                                y++;
                                if (y == 4) {
                                    z++;
                                    y = 0;
                                }
                            }
                        }
                    }

                }
            });

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
     * Method to allow image view variables to be effectively final
     * in order to change within eventhandler.
     * @param image the image view to be altered.
     * @return newEvent the mouseEvent to be applied to the imageView.
     */
    private EventHandler<MouseEvent> newImageHandler(ImageView image) {
        return newEvent -> {
            image.setOpacity(0.5);
            image.setOnMouseClicked(null);

            String url;
            url = image.getImage().getUrl();

            if (url.contains("cat")) {
                catCount++;
            }
            if (url.contains("dog")) {
                dogCount++;
            }
        };
    }


    /**
     * JSON method to store all of the cat images in the query.
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

            for (int x = 0; x < 30; x++) {
                catLink = get(root, x, "image", "url").getAsString();
                catArray[x] = catLink;
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
            System.out.println("cats exception");
        } // try
    }





    /**
     * JSON method to store all of the dog images in the query.
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
            for (int y = 0; y < 30; y++) {
                dogLink = get(root, y, "image", "url").getAsString();
                dogArray[y] = dogLink;
            }


        } catch (IOException ioe) {
            System.out.println(ioe);
            System.out.println("dogs exception");
        } // try
    }

    /**
     * Method to merge the dog and cat arrays into one array.
     */
    public void mergeArrays() {

        for (int x = 0; x < dogArray.length; x++) {
            fullArray[x] = dogArray[x];
        }
        for (int y = 0; y < catArray.length; y++) {
            fullArray[y + 30] = catArray[y];
        }
    }



} // OmegaApp
