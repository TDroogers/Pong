package nl.drogecode.pong;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Pong extends Application
{

  public static void main(String[] args)
  {
    launch(args);
  }

  @Override public void start(Stage primaryStage)
  {
    Settings settings = new Settings();
    WebSender webSender = new WebSender();
    primaryStage.setTitle("Pong");

    Group root = new Group();
    BorderPane pane = new BorderPane();
    Scene scene = setScene(settings, webSender, root, pane);

    primaryStage.setScene(scene);

    Score score = new Score(primaryStage, settings);
    Message message = new Message(primaryStage);

    Pause pause = new Pause(message);

    Beam rLeft = new Beam(settings, "Left", primaryStage, pause);
    Beam rRight = new Beam(settings, "Right", primaryStage, pause);

    // Create Circle
    Bal bal = new Bal(settings, primaryStage, rLeft, rRight, score, pause);

    root.getChildren().addAll(pane, score, bal, message, rLeft, rRight);

    primaryStage.setResizable(false);

    primaryStage.show();

    Controll contr = new Controll(rLeft, rRight, bal, score, message, pause);

    primaryStage.getScene().setOnKeyPressed(e ->
    {
      contr.setKey(e.getCode());
    });

    primaryStage.getScene().setOnKeyReleased(e ->
    {
      contr.setRelease(e.getCode());
    });

    MovableObjects movable = new MovableObjects(contr, rLeft, rRight, bal);

    webSender.setControll(movable);
    webSender.setPrimaryStage(primaryStage);
  }

  private Scene setScene(Settings settings, WebSender webSender, Group root, BorderPane pane)
  {
    /*
     * https://docs.oracle.com/javafx/2/ui_controls/menu_controls.htm
     */

    MenuBar menuBar = new MenuBar();
    Menu menuFile = new Menu("Online");
    MenuItem add = new MenuItem("Search connection");

    add.setOnAction(e -> webSender.connect());

    menuFile.getItems().addAll(add);
    menuBar.getMenus().addAll(menuFile);

    Scene scene = new Scene(root, settings.getGameWidth(), settings.getGameHight());
    pane.prefHeightProperty().bind(scene.heightProperty());
    pane.prefWidthProperty().bind(scene.widthProperty());

    pane.setTop(menuBar);

    return scene;
  }
}
