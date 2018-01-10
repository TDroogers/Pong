package nl.drogecode.pong.web.client;

import java.io.BufferedReader;
import java.io.IOException;

import javafx.application.Platform;
import nl.drogecode.pong.objects.MovableObjects;

public class ConsumerC extends Thread
{
  BufferedReader in;
  MovableObjects movable;

  private double oldSerY, oldBalX, oldBalY, oldDirX, oldDirY, curSerY, curBalX, curBalY, curDirX, curDirY;
  private int scoreL, scoreR;
  private boolean scored;

  public ConsumerC(BufferedReader in, MovableObjects movable)
  {
    this.in = in;
    this.movable = movable;
  }

  @Override public void run()
  {
    String fromServer;
    for (;;)
    {
      try
      {
        fromServer = in.readLine();
        if (fromServer.substring(0, 5).equals("hello"))
        {
          System.out.println("connection made");
          readFullString(fromServer);
          movable.setScoreRestart();
          movable.resetBal();
        }
        else
        {
          readString(fromServer);
        }
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }

  private void readFullString(String fromServer)
  {
    String[] ary = fromServer.split("/");
    try
    {
      oldSerY = Double.parseDouble(ary[1]);
      oldBalX = Double.parseDouble(ary[2]);
      oldBalY = Double.parseDouble(ary[3]);
      oldDirX = Double.parseDouble(ary[4]);
      oldDirY = Double.parseDouble(ary[5]);
      scoreL = Integer.parseInt(ary[6]);
      scoreR = Integer.parseInt(ary[7]);
    }
    catch (Exception e)
    {
      System.out.println("error woeps: " + e);
    }
    scored = true;
    updateScreen();
  }

  private void readString(String fromServer)
  {
    String[] ary = fromServer.split("/");
    try
    {
      curSerY = Double.parseDouble(ary[0]);
      curBalX = Double.parseDouble(ary[1]);
      curBalY = Double.parseDouble(ary[2]);
      curDirX = Double.parseDouble(ary[3]);
      curDirY = Double.parseDouble(ary[4]);

      if (ary.length > 5)
      {
        scoreL = Integer.parseInt(ary[5]);
        scoreR = Integer.parseInt(ary[6]);
        scored = true;
      }
    }
    catch (Exception e)
    {
      System.out.println("error woeps: " + e);
    }

    oldSerY += curSerY;
    oldBalX += curBalX;
    oldBalY += curBalY;
    oldDirX += curDirX;
    oldDirY += curDirY;

    updateScreen();
  }

  private void updateScreen()
  {
    Platform.runLater(new Runnable()
    {
      @Override public void run()
      {
        movable.setBeamLeftY(oldSerY);
        movable.setBalX(oldBalX);
        movable.setBalY(oldBalY);
        movable.setBalDirX(oldDirX);
        movable.setBalDirY(oldDirY);
        if (scored)
        {
          movable.setScoreLeft(scoreL);
          movable.setScoreRight(scoreR);
          scored = false;
        }
      }
    });
  }
}
