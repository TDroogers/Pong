package nl.drogecode.pong;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Beam extends Rectangle
{
  private int distanceFromBorder;
  private int hight;
  private int widthBeam;
  private int fromBoarder;

  private String position;
  private Stage stage;
  private Thread th;
  private Pause pause;
  private double change;
  private boolean down = false;

  public Beam(Settings settings, String position, Stage stage, Pause pause)
  {
    this.position = position;
    this.stage = stage;
    this.pause = pause;

    distanceFromBorder = settings.getDistanceFromBorder();
    hight = settings.getHight();
    widthBeam = settings.getWidthBeam();

    restart();
  }

  public void restart()
  {
    setY(135 - (hight / 2));
    setWidth(widthBeam);
    setHeight(hight);
    switch (position)
    {
      case "Right":
        setRight();
        break;
      case "Left":
        setLeft();
        break;
      default:
        break;
    }
  }

  public void startY(double change)
  {
    if (!down)
    {
      this.change = change;
      initiateChangeY();
      down = true;
    }
  }

  public void stopY()
  {
    th.interrupt();
    down = false;
  }

  public int getDistanceFromBorder()
  {
    return fromBoarder;
  }

  private void setRight()
  {
    double width = stage.getScene().getWidth();
    fromBoarder = (int) (width - distanceFromBorder);
    setX(fromBoarder - widthBeam);
  }

  private void setLeft()
  {
    fromBoarder = distanceFromBorder;
    setX(fromBoarder);
  }

  private void initiateChangeY()
  {
    Task<Void> task = new Task<Void>()
    {
      @Override protected Void call() throws Exception
      {
        changeY();
        return null;
      }
    };
    th = new Thread(task);
    th.setDaemon(true);
    th.start();
  }

  private void changeY()
  {
    while (true)
    {
      if (pauzCheck())
      {
        continue;
      }
      double y = getY();
      double neww = y + change;
      double max = stage.getScene().getHeight() - hight;
      if (neww > max || neww < 0 || Thread.currentThread().isInterrupted())
      {
        break;
      }
      Platform.runLater(new Runnable()
      {
        @Override public void run()
        {
          setY(neww);
        }
      });
      Sleeper sleep = new Sleeper();
      if (!sleep.sleeper())
      {
        break;
      }
    }
  }

  private boolean pauzCheck()
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
}
