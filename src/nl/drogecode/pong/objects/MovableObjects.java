package nl.drogecode.pong.objects;

import nl.drogecode.pong.Controll;
import nl.drogecode.pong.text.Message;
import nl.drogecode.pong.text.Score;

public class MovableObjects
{
  private Controll contr;
  private Beam rLeft;
  private Beam rRight;
  private Bal bal;
  private Score score;
  private Message message;

  public MovableObjects(Controll contr, Beam rLeft, Beam rRight, Bal bal, Score score, Message message)
  {
    this.contr = contr;
    this.rLeft = rLeft;
    this.rRight = rRight;
    this.bal = bal;
    this.score = score;
    this.message = message;
  }

  /*
   * First the getters.
   */
  public double getBeamRightX()
  {
    return rRight.getX();
  }

  public int getChangeR()
  {
    return (int) rRight.getChange();
  }

  public double getBeamRightY()
  {
    return rRight.getY();
  }

  public int getChangeL()
  {
    return (int) rLeft.getChange();
  }

  public double getBeamLeftX()
  {
    return rLeft.getX();
  }

  public double getBeamLeftY()
  {
    return rLeft.getY();
  }

  public double getBalX()
  {
    return bal.getCenterX();
  }

  public double getBalY()
  {
    return bal.getCenterY();
  }

  public double getBalDirX()
  {
    return bal.getDirX();
  }

  public double getBalDirY()
  {
    return bal.getDirY();
  }

  public String getScore()
  {
    return score.getText();
  }

  public int getScoreRight()
  {
    return score.getScoreRight();
  }

  public int getScoreLeft()
  {
    return score.getScoreLeft();
  }

  /*
   * Setters.
   */

  public void setPlayer(String side)
  {
    contr.setPlayer(side);
    message.setDifferentMessage(side);

    switch (side)
    {
      case "client":
        bal.setClient(true);
        bal.nextGame();
        rLeft.startY(0);
        break;
        
      case "server":
        rRight.startY(0);
        //fall in default!!!
        
      default:
        bal.setClient(false);
        break;
    }
  }

  public void setBeamRightY(double newY, int beamYChange)
  {
    rRight.setY(newY);
    rRight.setChange(beamYChange);
  }

  public void setBeamLeftY(double newY, int beamYChange)
  {
    rLeft.setY(newY);
    rLeft.setChange(beamYChange);
  }

  public void setBalX(double newX)
  {
    bal.setX(newX);
  }

  public void setBalY(double newY)
  {
    bal.setY(newY);
  }

  public void setBalDirX(double dir)
  {
    bal.setDirX(dir);
  }

  public void setBalDirY(double dir)
  {
    bal.setDirY(dir);
  }

  public void setScoreRestart()
  {
    this.score.restart();
  }

  public void setScoreRight(int right)
  {
    System.out.println("right: " + right);
    this.score.setScoreRight(right);
  }

  public void setScoreLeft(int left)
  {
    System.out.println("left: " + left);
    this.score.setScoreLeft(left);
  }
}
