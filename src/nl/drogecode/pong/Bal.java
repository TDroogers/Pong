package nl.drogecode.pong;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;

public class Bal extends Bal_setUp
{

  private int clear = 0;
  private double maxX;
  private double maxY;
  private double x;
  private double y;
  private boolean firstSpeedUp = true;
  private boolean restart = false;

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
      th.interrupt();
    }
    startMove();
  }

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
    if (!setDirection(0))
    {
      return;
    }
    while (true)
    {
      if (pauzCheck())
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
    x = getCenterX();
    y = getCenterY();

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

    Sleeper sleep = new Sleeper();
    if (!sleep.sleeper((int) speedUp))
    {
      return false;
    }

    return true;
  }

  private boolean direction()
  {
    newX = x + dirX;
    newY = y + dirY;

    intersect();

    if (newY + getRadius() > maxY || newY - getRadius() < 0)
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
      if (!score.setScoreLeft())
      {
        return false;
      }
      nextRound();
    }
    else if (newX > maxX && rOrL == 1)
    {
      if (!score.setScoreRight())
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

    // System.out.println(calc + " " + speedUp + " " + speed);

    speedUp();
    clear = 3;
  }

  private void speedUp()
  {
    if (speedUp > 30)
    {
      speedUp--;
    }
    else if (speed <= 60)
    {
      if (firstSpeedUp)
      {
        firstSpeedUp = false;
      }
      speed += 0.3;
    }
    else
    {
      // System.out.println("max speed, good luck! " + speed);
    }
  }
}
