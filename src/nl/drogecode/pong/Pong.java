package nl.drogecode.pong;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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

    Group root = new Group();
    Scene scene = setScene(settings, root);

    primaryStage.setScene(scene);

    Score score = new Score(primaryStage, settings);
    Message message = new Message(primaryStage);

    Pause pause = new Pause(message);

    Beam rLeft = new Beam(settings, "Left", primaryStage, pause);
    Beam rRight = new Beam(settings, "Right", primaryStage, pause);

    // Create Circle
    Bal bal = new Bal(settings, primaryStage, rLeft, rRight, score, pause);

    root.getChildren().addAll(score, bal, message, rLeft, rRight);

    primaryStage.setResizable(false);

    primaryStage.setTitle("Pong");
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
  }

  private Scene setScene(Settings settings, Group root)
  {
    
    Scene scene = new Scene(root, settings.getGameWidth(), settings.getGameHight());
    
    MenuBar menuBar = new MenuBar();
    Menu menuFile = new Menu("Online");
    MenuItem add = new MenuItem("Search connection");
    
    WebSender webSender = new WebSender();
    add.setOnAction(e->webSender.connect());
    
    menuFile.getItems().addAll(add);
    menuBar.getMenus().addAll(menuFile);

    ((Group) scene.getRoot()).getChildren().addAll(menuBar);
    return scene;
  }
}
