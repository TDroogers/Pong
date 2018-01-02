package nl.drogecode.pong;

public class MovableObjects
{
  private Controll contr;
  private Beam rLeft;
  private Beam rRight;
  private Bal bal;

  public MovableObjects(Controll contr, Beam rLeft, Beam rRight, Bal bal)
  {
    this.contr = contr;
    this.rLeft = rLeft;
    this.rRight = rRight;
    this.bal = bal;
  }
  
  public void setPlayer(String side)
  {
    contr.setPlayer(side);
    if (side.equals("client"))
    {
      bal.setClient();
    }
  }
  
  /*
   * First the getters.
   */
  public double getBeamRightX()
  {
    return rRight.getBeamX();
  }
  
  public double getBeamLeftX()
  {
    return rLeft.getBeamX();
  }
  
  public double getBeamRightY()
  {
    return rRight.getBeamY();
  }
  
  public double getBeamLeftY()
  {
    return rLeft.getBeamY();
  }
  
  /*
   * Setters.
   */
  public void setBeamRightX(double newX)
  {
    rRight.setBeamX(newX);
  }
  
  public void setBeamRightY(double newY)
  {
    rRight.setBeamY(newY);
  }
}
