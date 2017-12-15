package pong;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
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
    primaryStage.setScene(new Scene(root, settings.getGameWidth(), settings.getGameHight()));

    Score score = new Score(primaryStage, settings);
    Message message = new Message(primaryStage);

    Pause pause = new Pause(message);

    Beam rLeft = new Beam(settings, "Left", primaryStage, pause);
    Beam rRight = new Beam(settings, "Right", primaryStage, pause);

    // Create Circle
    Bal bal = new Bal(settings, primaryStage, rLeft, rRight, score, pause);

    root.getChildren().addAll(score, message, bal, rLeft, rRight);

    primaryStage.setResizable(false);

    primaryStage.setTitle("Pong");
    primaryStage.show();

    Controll contr = new Controll(rLeft, rRight, bal, score, message, pause);

    primaryStage.getScene().setOnKeyPressed(e ->
    {
      // System.out.println("e = " + e);
      contr.setKey(e.getCode());
    });

    primaryStage.getScene().setOnKeyReleased(e ->
    {
      // System.out.println("e = " + e);
      contr.setRelease(e.getCode());
    });
  }

  protected void finalize()
  {
    System.out.println("finalize");
  }
}
