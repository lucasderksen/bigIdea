package nl.fhict.s3.websocketclient.singleplayer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import nl.fhict.s3.restclient.SimpleRestClient;
import nl.fhict.s3.sharedmodel.GameInfo;
import nl.fhict.s3.sharedmodel.User;
import nl.fhict.s3.websocketclient.WebsocketClient;

import java.util.*;

public class Main extends Application implements Observer {

    private WebsocketClient websocketClient;
    private SimpleRestClient restClient;

    private static int TILE_SIZE = 80;
    private static int COLUMNS = 7;
    private static int ROWS = 6;

    private boolean redMove = false;
    private boolean hasWon = false;
    private boolean myTurn = false;

    private Stage stage;
    private Pane discRoot = new Pane();

    // single player logic
    private SPLogic spLogic = new SPLogic(TILE_SIZE, COLUMNS, ROWS);
    private Disc[][] grid = spLogic.getGrid();

    private Parent createContent() {
        Pane root = new Pane();
        root.getChildren().add(discRoot);

        Shape gridShape = makeGrid();
        root.getChildren().add(gridShape);
        root.getChildren().addAll(makeColumns());

        return root;
    }

    private void createLoginForm(Stage stage) {
        // Create the VBox
        VBox root = new VBox();
        // Set the Size of the VBox
        root.setMinSize(250, 150);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));

        Label lblUser = new Label("Username");
        grid.add(lblUser, 0, 1);

        TextField txtUser = new TextField();
        txtUser.setPromptText("username");
        grid.add(txtUser, 1, 1);

        Label lblPassword = new Label("Password");
        grid.add(lblPassword, 0, 2);

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("password");
        grid.add(txtPassword, 1, 2);

        Button loginBtn = new Button("  Login  ");
        grid.add(loginBtn, 1, 3);

        Button registerBtn = new Button("Register");
        grid.add(registerBtn, 1, 4);

        //databaseConnection.addUser(txtUser.getText(),txtPassword.getText());
        loginBtn.setOnAction(e -> {
            if (restClient.postUserExist(new User(txtUser.getText(), txtPassword.getText())) != null) {
                // wellicht alert
                stage.setScene(new Scene(createContent()));
                stage.setTitle("4 op een rij - " + txtUser.getText());
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Incorrect credentials");
                alert.setHeaderText("Perhaps try entering credentials again.");
                alert.setContentText("If you do not have an account yet, register!");
                alert.showAndWait();

            }
        });
        registerBtn.setOnAction(e -> {
            //databaseConnection.addUser(txtUser.getText(), txtPassword.getText());
            restClient.postUser(new User(txtUser.getText(), txtPassword.getText()));
            // wellicht check als goed in db staat
            stage.setScene(new Scene(createContent()));
            stage.setTitle("4 op een rij - " + txtUser.getText());
            stage.show();
        });

        root.getChildren().add(grid);

        // Create the Scene
        Scene scene = new Scene(root);

        // Set the Properties of the Stage
        stage.setX(100);
        stage.setY(200);
        stage.setMinHeight(200);
        stage.setMinWidth(250);

        // Add the scene to the Stage
        stage.setScene(scene);
        // Set the title of the Stage
        stage.setTitle("4 op 1 rij inlog");
        // Display the Stage
        stage.show();
    }

    private Shape makeGrid() {
        Shape shape = new Rectangle((double) (COLUMNS + 1) * TILE_SIZE, (double) (ROWS + 1) * TILE_SIZE);

        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                Circle circle = new Circle(TILE_SIZE / (double)2);
                circle.setCenterX(TILE_SIZE / (double)2);
                circle.setCenterY(TILE_SIZE / (double)2);
                circle.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / (double)4);
                circle.setTranslateY(y * (TILE_SIZE + 5) + TILE_SIZE / (double)4);

                shape = Shape.subtract(shape, circle);
            }
        }

        shape.setFill(Color.BLUE);

        return shape;
    }

    private List<Rectangle> makeColumns() {
        List<Rectangle> list = new ArrayList<>();

        for (int x = 0; x < COLUMNS; x++) {
            Rectangle rect = new Rectangle(TILE_SIZE, (double) (ROWS + 1) * TILE_SIZE);
            rect.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / (double)4);
            rect.setFill(Color.TRANSPARENT);

            rect.setOnMouseEntered(e -> rect.setFill(Color.rgb(200, 200, 50, 0.3)));
            rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));

            final int column = x;
            rect.setOnMouseClicked(e -> placeDisc(new Disc(redMove, TILE_SIZE), column));

            list.add(rect);
        }

        return list;
    }

    private void placeDisc(Disc disc, int column) {

        if (myTurn) {
            int row = ROWS - 1;
            do {
                if (!getDisc(column, row).isPresent())
                    break;

                row--;
            } while (row >= 0);

            if (row < 0)
                return;

            hasWon = spLogic.placeDisc(column, row, disc);


            discRoot.getChildren().add(disc);
            disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / (double)4);
            disc.setTranslateY(row * (TILE_SIZE + 5) + TILE_SIZE / (double)4);

            myTurn = !myTurn;

            // wellicht return op place disc met if statement if win true bool
            websocketClient.sendTurn(disc, column, row);
            winPopup(redMove);

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText("Het is op dit moment niet jouw beurt");

            alert.showAndWait();
        }

    }

    private Optional<Disc> getDisc(int column, int row) {
        if (column < 0 || column >= COLUMNS
                || row < 0 || row >= ROWS)
            return Optional.empty();

        return Optional.ofNullable(grid[column][row]);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // wellicht websocket code ergens anders starten

        this.stage = stage;

        websocketClient = new WebsocketClient();
        websocketClient.start();
        websocketClient.greeterClientEndpoint.addObserver(this);

        restClient = new SimpleRestClient();
        restClient.postUser(new User("test", "test"));
        createLoginForm(stage);


    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(Observable o, Object arg) {
        GameInfo gameInfo = ((GameInfo) arg);
        switch (gameInfo.getMessageType()) {
            case "heyMessage":
                break;

            case "rematch":
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Geen rematch");
                    alert.setHeaderText("de tegenstander wilt niet opnieuw spelen!");
                    alert.setContentText("Het spel zal nu gesloten worden.");
                    alert.showAndWait();
                    System.exit(0);
                });

                break;
            case "sendTurn":
                Disc disc = new Disc(gameInfo.isRed(), TILE_SIZE);
                int column = gameInfo.getColumn();
                int row = gameInfo.getRow();

                Platform.runLater(() -> {
                    hasWon = spLogic.placeDisc(column, row, disc);
                    discRoot.getChildren().add(disc);
                    disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / (double)4);
                    disc.setTranslateY(row * (TILE_SIZE + 5) + TILE_SIZE / (double)4);

                    myTurn = true;

                    winPopup(gameInfo.isRed());
                });

                break;
            case "youAre":
                redMove = gameInfo.isRed();
                // als rood true anders false en yellow
                myTurn = redMove;
                break;
            default:
                // code block;
        }
    }

    public boolean winPopup(Boolean isRed) {
        if (spLogic.rematch()) {

            ButtonType foo = new ButtonType("Ja", ButtonBar.ButtonData.OK_DONE);
            ButtonType bar = new ButtonType("Nee", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Wil je een rematch? Zo niet dan sluit de applicatie.",
                    foo, bar);

            alert.setHeaderText("Rematch?");
            alert.setTitle("Rematch?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.orElse(bar) == foo) {
                spLogic = new SPLogic(TILE_SIZE, COLUMNS, ROWS);
                grid = spLogic.getGrid();
                discRoot = new Pane();
                stage.setScene(new Scene(createContent()));
                stage.setTitle("4 op een rij - rematch");
                stage.show();
                return true;
            } else {
                websocketClient.rematch(false);
                System.exit(0);
                return false;
            }

        }
        return false;
    }
}