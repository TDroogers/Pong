package nl.drogecode.pong.objects;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import nl.drogecode.pong.*;
import nl.drogecode.pong.text.Score;

public class Bal extends Bal_setUp
{

  private volatile int clear = 0;
  private volatile double maxX;
  private volatile double maxY;
  private volatile double x;
  private volatile double y;
  private volatile boolean firstSpeedUp = true;
  private volatile boolean restart = false;
  private volatile boolean breaker = false;
  private volatile boolean client = false;

  public Bal(Settings settings, Stage stage, Beam left, Beam right, Score score, Pause pause)
  {
    super(settings, stage, left, right, score, pause);
  }

  public void nextGame()
  {
    restart = true;
    centerBall();
    if (th != null)
    {
      breaker = true;
    }
    startMove();
  }

  public void setClient(boolean bool)
  {
    client = bool;
  }

  public double getDirX()
  {
    return dirX;
  }

  public double getDirY()
  {
    return dirY;
  }

  public void setDirX(double dir)
  {
    dirX = dir;
  }

  public void setDirY(double dir)
  {
    dirY = dir;
  }

  /*
   * private stuff from here.
   */

  private void startMove()
  {
    Task<Void> task = new Task<Void>()
    {
      @Override protected Void call() throws Exception
      {
        initiateLoop();
        return null;
      }
    };
    th = new Thread(task);
    th.setDaemon(true);
    th.start();
  }

  private void initiateLoop()
  {
    maxX = stage.getScene().getWidth();
    maxY = stage.getScene().getHeight();
    x = getCenterX();
    y = getCenterY();
    if (!setDirection(0)) // extended from Bal_setUp.java
    {
      return;
    }
    while (true)
    {
      if (pauzCheck()) // extended from Bal_setUp.java
      {
        continue;
      }
      if (!change())
      {
        break;
      }
    }
  }

  private boolean change()
  {
    if (restart)
    {
      rOrL = 0;
      nextRoundStart();
      restart = false;
    }

    if (!direction())
    {
      return false;
    }

    updateBal();

    if (!sleep.sleeper((int) speedUp) || breaker || client)
    {
      breaker = false;
      return false;
    }

    x = newX;
    y = newY;

    return true;
  }

  private boolean direction()
  {
    newX = x + dirX;
    newY = y + dirY;

    intersect();

    if (newY + getRadius() > maxY || newY - getRadius() < 25)
    {
      dirY *= -1;
      newY = y;
    }
    else if (clear <= 0)
    {
      if (!getPointCheck())
      {
        return false;
      }
    }
    return true;
  }

  private boolean getPointCheck()
  {
    if (newX < 0 && rOrL == -1)
    {
      if (!score.setScoreLeftPlus())
      {
        return false;
      }
      nextRound();
    }
    else if (newX > maxX && rOrL == 1)
    {
      if (!score.setScoreRightPlus())
      {
        return false;
      }
      nextRound();
    }

    return true;
  }

  private void updateBal()
  {
    Platform.runLater(new Runnable()
    {
      @Override public void run()
      {
        setCenterX(newX);
        setCenterY(newY);
      }
    });
  }

  private void intersect()
  {
    if (clear <= 0)
    {
      intersectTest();
    }
    else // if (clear > 0)
    {
      clear--;
    }
  }

  private void intersectTest()
  {
    boolean bounce = false;
    double bouncePoint = 0;
    double bounceHight = 0;

    double centerX = newX;
    double balXRight = centerX + getRadius();
    double balXLeft = centerX - getRadius();

    double centerY = newY;
    double balYTop = centerY + getRadius();
    double balYBottom = centerY - getRadius();

    double leftX = left.getX() + left.getWidth();
    double leftYBottom = left.getY();
    double leftYTop = leftYBottom + left.getHeight();

    double rightX = right.getX();
    double rightYBottom = right.getY();
    double rightYTop = rightYBottom + right.getHeight();

    if (balXLeft < leftX && leftYTop > balYBottom && leftYBottom < balYTop
        && centerX > left.getDistanceFromBorder() - speed)
    {
      bounce = true;
      bouncePoint = centerY - leftYBottom;
      bounceHight = left.getHeight();
    }
    else if (balXRight > rightX && rightYTop > balYBottom && rightYBottom < balYTop
        && centerX < right.getDistanceFromBorder() + speed)
    {
      bounce = true;
      bouncePoint = centerY - rightYBottom;
      bounceHight = right.getHeight();
    }

    if (bounce == true)
    {
      bounce(bouncePoint, bounceHight);
    }
  }

  private void bounce(double bouncePoint, double bounceHight)
  {
    double bounce = bouncePoint - bounceHight / 2;
    double calc = (((100 / (bounceHight / 2)) * bounce) / 100);

    newX = x;
    newY = y;
    if (calc > 1)
    {
      calc = 1;
    }
    else if (calc < -1)
    {
      calc = -1;
    }

    setDirection(calc);

    speedUp();
    clear = 3;
  }

  private void speedUp()
  {
    if (speedUp > 30)
    {
      speedUp = speedUp - 2;
    }
    else if (speed <= 60)
    {
      if (firstSpeedUp)
      {
        firstSpeedUp = false;
      }
      speed += 0.5;
    }
  }
}
