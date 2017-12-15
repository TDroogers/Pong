package pong;

public class Settings
{
  // scene
  private int gameWidth = 700;
  private int gameHight = 380;

  // beam
  private int distanceFromBorder = 30;
  private int hight = 50;
  private int widthBeam = 5;

  // bal
  private double startSpeed = 5;
  private double startSpeedUp = 35;

  // score
  private int winScore = 3;

  /*
   * 
   * 
   * 
   * 
   * 
   * 
   */

  // scene
  public int getGameWidth()
  {
    return gameWidth;
  }

  public int getGameHight()
  {
    return gameHight;
  }

  // beam
  public int getDistanceFromBorder()
  {
    return distanceFromBorder;
  }

  public int getHight()
  {
    return hight;
  }

  public int getWidthBeam()
  {
    return widthBeam;
  }

  // bal

  public double getStartSpeed()
  {
    return startSpeed;
  }

  public double getStartSpeedUp()
  {
    return startSpeedUp;
  }

  // score

  public int getWinScore()
  {
    return winScore;
  }
}
