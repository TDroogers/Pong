/*
 * from: https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
 */
package nl.drogecode.pong.web.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Platform;
import nl.drogecode.pong.Sleeper;
import nl.drogecode.pong.objects.MovableObjects;

public class WebClient
{
  private MovableObjects movable;
  private double previus, currentY, oldSerY, oldBalX, oldBalY, oldDirX, oldDirY, curSerY, curBalX, curBalY, curDirX,
      curDirY;
  private int scoreL, scoreR;
  private byte ret;
  private boolean scored;

  public boolean client(MovableObjects movable) throws IOException
  {
    this.movable = movable;
    WebClientUdp udp = new WebClientUdp();
    String hostName = udp.getBroadcast();
    Sleeper sleep = new Sleeper();

    if (hostName.equals(""))
    {
      return false;
    }
    System.out.println(hostName);

    int portNumber = 2315;

    try (Socket kkSocket = new Socket(hostName, portNumber);
        PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));)
    {
      String fromServer;
      movable.setPlayer("client");

      while ((fromServer = in.readLine()) != null)
      {
        if (fromServer.substring(0, 5).equals("hello"))
        {
          System.out.println("connection made");
          previus = movable.getBeamRightY();
          readFullString(fromServer);
        }
        else
        {
          readString(fromServer);
        }

        out.println(toServerByte());
        sleep.sleeper(30);
      }
    }
    catch (UnknownHostException e)
    {
      System.err.println("Don't know about host " + hostName);
      movable.setPlayer("co-op");
    }
    catch (IOException e)
    {
      System.err.println("Couldn't get I/O for the connection to " + hostName);
      movable.setPlayer("co-op");
    }
    return true;
  }

  /*
   * data efficient version
   */
  private byte toServerByte()
  {
    currentY = movable.getBeamRightY();
    ret = (byte) (currentY - previus);
    previus = currentY;
    return ret;
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
    
    movable.setScoreRestart();
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
        if(scored)
        {
          movable.setScoreLeft(scoreL);
          movable.setScoreRight(scoreR);
          scored = false;
        }
      }
    });
  }
}
