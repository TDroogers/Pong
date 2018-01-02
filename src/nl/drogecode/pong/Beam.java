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

  private double change;
  private double beamSpeed;

  private volatile boolean breaker = false;

  private String position;
  private Stage stage;
  private Thread th;
  private Pause pause;

  public Beam(Settings settings, String position, Stage stage, Pause pause)
  {
    this.position = position;
    this.stage = stage;
    this.pause = pause;

    distanceFromBorder = settings.getDistanceFromBorder();
    hight = settings.getHight();
    widthBeam = settings.getWidthBeam();
    beamSpeed = settings.getBeamSpeed();

    restart();
  }

  public void restart()
  {
    setY(stage.getScene().getHeight() / 2 - (hight / 2));
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

  public void startY(int change)
  {
    this.change = change * beamSpeed;
    if (th == null || !th.isAlive())
    {
      initiateChangeY();
    }
  }

  public void stopY(int change)
  {
    if (this.change == change * beamSpeed)
    {
      this.change = 0;
      breaker = true;
    }
  }

  public int getDistanceFromBorder()
  {
    return fromBoarder;
  }
  
  /*
   * External controll functions
   */
  
  public double getBeamX()
  {
    return getX();
  }
  
  public double getBeamY()
  {
    return getY();
  }
  
  public void setBeamX(double newX)
  {
    setX(newX);
  }
  
  public void setBeamY(double newY)
  {
    setY(newY);
  }
  
  /*
   * private function's
   */

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
    double max = stage.getScene().getHeight() - hight;
    Sleeper sleep = new Sleeper();
    while (true)
    {
      if (pauzCheck(sleep))
      {
        continue;
      }
      double y = getY();
      double neww = y + change;
      if (neww > max || neww < 25)
      {
        sleep.sleeper();
        continue;
      }
      runLater((short) 0, neww);
      if (!sleep.sleeper() || breaker || Thread.currentThread().isInterrupted())
      {
        runLater((short) 1, neww);
        break;
      }
    }
  }

  private void runLater(short s, double neww)
  {
    Platform.runLater(new Runnable()
    {
      @Override public void run()
      {
        switch (s)
        {
          case 0:
            setY(neww);
            break;

          case 1:
            breaker = false;
            break;
        }

      }
    });
  }

  private boolean pauzCheck(Sleeper sleep)
  {
    if (pause.getPause())
    {
      sleep.sleeper(100);
      return true;
    }
    else
      return false;
  }
}
