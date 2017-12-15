package nl.drogecode.pong;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Bal_setUp extends Circle
{

  protected Thread th;
  protected Stage stage;
  protected Beam left;
  protected Beam right;
  protected Score score;
  protected Pause pause;
  protected double speedUp;
  protected double newX;
  protected double newY;
  protected double startX;
  protected double startY;
  protected double startSpeedUp;
  protected double startSpeed;
  protected double dirX;
  protected double dirY;
  protected double speed;
  protected double rOrL = 0;

  public Bal_setUp(Settings settings, Stage stage, Beam left, Beam right, Score score, Pause pause)
  {
    this.stage = stage;
    this.left = left;
    this.right = right;
    this.score = score;
    this.pause = pause;

    startX = stage.getScene().getWidth() / 2;
    startY = stage.getScene().getHeight() / 2;

    startSpeed = settings.getStartSpeed();
    startSpeedUp = settings.getStartSpeedUp();

    centerBall();

    setFill(Color.RED);
    setRadius(5.0);
  }

  protected void centerBall()
  {
    setCenterX(startX);
    setCenterY(startY);
  }

  protected boolean pauzCheck()
  {
    if (pause.getPause())
    {
      Sleeper sleep = new Sleeper();
      sleep.sleeper(100);
      return true;
    }
    else
      return false;
  }


  protected boolean setDirection(double dir)
  {
    double startDir = dir;
    if (dir == 0)
    {
      startDir = Math.random() * Math.PI / 2 - Math.PI / 4;
    }
    if (rOrL == 0)
    {
      rOrL = Math.signum(Math.random() - 0.5);
    }
    else
    {
      rOrL *= -1;
    }
    dirX = Math.cos(startDir) * speed * rOrL;
    dirY = Math.sin(startDir) * speed;

    return true;
  }

  protected void nextRound()
  {
    Sleeper sleep = new Sleeper();
    sleep.sleeper(1500);
    nextRoundStart();
  }

  protected void nextRoundStart()
  {
    newX = startX;
    newY = startY;
    speedUp = startSpeedUp;
    speed = startSpeed;
    setDirection(0);
  }
}
