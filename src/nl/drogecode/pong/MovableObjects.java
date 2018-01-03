package nl.drogecode.pong;

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
    return rRight.getBeamX();
  }
  
  public double getBeamRightY()
  {
    return rRight.getBeamY();
  }
  
  public double getBeamLeftX()
  {
    return rLeft.getBeamX();
  }
  
  public double getBeamLeftY()
  {
    return rLeft.getBeamY();
  }
  
  public double getBalX()
  {
    return bal.getOldX();
  }
  
  public double getBalY()
  {
    return bal.getOldY();
  }
  
  public String getScore()
  {
    return score.getText();
  }
  
  /*
   * Setters.
   */
  
  public void setPlayer(String side)
  {
    contr.setPlayer(side);
    message.setDifferentMessage(side);
    if (side.equals("client"))
    {
      bal.setClient();
    }
  }
  
  public void setBeamRightX(double newX)
  {
    rRight.setBeamX(newX);
  }
  
  public void setBeamRightY(double newY)
  {
    rRight.setBeamY(newY);
  }
  
  public void setBeamLeftX(double newX)
  {
    rLeft.setBeamX(newX);
  }
  
  public void setBeamLeftY(double newX)
  {
    rLeft.setBeamY(newX);
  }
  
  public void setBalX(double newX)
  {
    bal.setNewX(newX);
  }
  
  public void setBalY(double newY)
  {
    bal.setNewY(newY);
  }
  
  public void setScore(String score)
  {
    this.score.setText(score);
  }
}
