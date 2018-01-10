package nl.drogecode.pong.web.client;

import java.io.PrintWriter;

import nl.drogecode.pong.Sleeper;
import nl.drogecode.pong.objects.MovableObjects;

public class ProducerC extends Thread
{
  PrintWriter out;
  MovableObjects movable;
  

  private double previus, currentY;
  private byte ret;
  
  
  public ProducerC (PrintWriter out, MovableObjects movable)
  {
    this.out = out;
    this.movable = movable;
  }

  @Override public void run()
  {
    Sleeper sleep = new Sleeper();
    previus = movable.getBeamRightY();

    for(;;)
    {
      out.println(toServerByte());
      sleep.sleeper(Long.MAX_VALUE);
    }
  }
  

  private byte toServerByte()
  {
    currentY = movable.getBeamRightY();
    ret = (byte) (currentY - previus);
    previus = currentY;
    return ret;
  }
}
