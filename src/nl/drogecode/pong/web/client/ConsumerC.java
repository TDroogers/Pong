package nl.drogecode.pong.web.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import javafx.application.Platform;
import nl.drogecode.pong.objects.MovableObjects;

public class ConsumerC extends Thread
{
  Socket socket;
  MovableObjects movable;

  private double oldSerY, oldBalX, oldBalY, oldDirX, oldDirY, curSerY, curBalX, curBalY, curDirX, curDirY;
  private int scoreL, scoreR, beamYChange;
  private boolean scored;

  public ConsumerC(Socket socket, MovableObjects movable)
  {
    this.socket = socket;
    this.movable = movable;
  }

  @Override public void run()
  {
    String fromServer;

    try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));)
    {
      for (;;)
      {
        fromServer = in.readLine();
        if (fromServer.substring(0, 5).equals("hello"))
        {
          System.out.println("connection made");
          readFullString(fromServer);
          movable.setScoreRestart();
        }
        else
        {
          readString(fromServer);
        }
      }
    }
    catch(SocketException e)
    {
      System.err.println("connection lost with server: " + e);
      movable.setPlayer("co-op");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void readFullString(String fromServer)
  {
    String[] ary = fromServer.split("/");
    try
    {
      oldSerY = Double.parseDouble(ary[1]);
      beamYChange = Integer.parseInt(ary[2]);
      oldBalX = Double.parseDouble(ary[3]);
      oldBalY = Double.parseDouble(ary[4]);
      oldDirX = Double.parseDouble(ary[5]);
      oldDirY = Double.parseDouble(ary[6]);
      scoreL = Integer.parseInt(ary[7]);
      scoreR = Integer.parseInt(ary[8]);
    }
    catch (Exception e)
    {
      System.out.println("error ConsumerC readFullString: " + fromServer + " : " + e);
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
      beamYChange = Integer.parseInt(ary[1]);
      curBalX = Double.parseDouble(ary[2]);
      curBalY = Double.parseDouble(ary[3]);
      curDirX = Double.parseDouble(ary[4]);
      curDirY = Double.parseDouble(ary[5]);

      if (ary.length > 6)
      {
        scoreL = Integer.parseInt(ary[6]);
        scoreR = Integer.parseInt(ary[7]);
        scored = true;
      }
    }
    catch (Exception e)
    {
      System.out.println("error ConsumerC readString: " + fromServer + " : " + e);
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
        movable.setBeamLeftY(oldSerY, beamYChange);
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
