package nl.drogecode.pong.web.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import nl.drogecode.pong.Sleeper;
import nl.drogecode.pong.objects.MovableObjects;

public class ProducerS extends Thread
{
  private Socket socket = null;
  private MovableObjects movable;
  String inputLine, outputLine;
  private byte oldScoreL, oldScoreR, curScoreL, curScoreR;
  private double oldSerY, oldBalX, oldBalY, oldDirX, oldDirY;
  private double curSerY, curBalX, curBalY, curDirX, curDirY;

  public ProducerS(Socket socket, MovableObjects movable)
  {
    super("KKMultiServerThread " + socket.getLocalPort());
    this.socket = socket;
    this.movable = movable;
    oldSerY = movable.getBeamLeftY();
    oldBalX = movable.getBalX();
    oldBalY = movable.getBalY();
    oldDirX = movable.getBalDirX();
    oldDirY = movable.getBalDirY();
    oldScoreL = (byte) movable.getScoreLeft();
    oldScoreR = (byte) movable.getScoreRight();
  }

  @Override public void run()
  {
    try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);)
    {
      Sleeper sleep = new Sleeper();
      outputLine = toClintFullString();
      out.println("hello/" + outputLine);

      for (;;)
      {
        outputLine = toClientString();
        out.println(outputLine);

        yield();
        sleep.sleeper(Long.MAX_VALUE);

      }
    }
    catch (IOException e)
    {
      System.out.println("connection lost " + e);
      movable.setPlayer("co-op");
    }
    finally
    {
      try
      {
        socket.close();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }

  /*
   * data efficient version
   */

  private String toClintFullString()
  {
    StringBuilder ret = new StringBuilder("");

    ret.append(String.valueOf(oldSerY)).append("/");
    ret.append(String.valueOf(oldBalX)).append("/");
    ret.append(String.valueOf(oldBalY)).append("/");
    ret.append(String.valueOf(oldDirX)).append("/");
    ret.append(String.valueOf(oldDirY)).append("/");
    ret.append(String.valueOf(curScoreL)).append("/");
    ret.append(String.valueOf(curScoreR));

    String retu = ret.toString();
    return retu;
  }

  private String toClientString()
  {
    StringBuilder ret = new StringBuilder("");
    curSerY = movable.getBeamLeftY();
    curBalX = movable.getBalX();
    curBalY = movable.getBalY();
    curDirX = movable.getBalDirX();
    curDirY = movable.getBalDirY();
    curScoreL = (byte) movable.getScoreLeft();
    curScoreR = (byte) movable.getScoreRight();

    ret.append(String.valueOf((byte) (curSerY - oldSerY))).append("/");
    ret.append(String.valueOf((curBalX - oldBalX))).append("/");
    ret.append(String.valueOf((curBalY - oldBalY))).append("/");
    ret.append(String.valueOf((curDirX - oldDirX))).append("/");
    ret.append(String.valueOf((curDirY - oldDirY)));

    if (oldScoreL != curScoreL || oldScoreR != curScoreR)
    {
      ret.append("/").append(String.valueOf((byte) (curScoreL)));
      ret.append("/").append(String.valueOf((byte) (curScoreR)));
      oldScoreL = curScoreL;
      oldScoreR = curScoreR;
    }

    oldSerY = curSerY;
    oldBalX = curBalX;
    oldBalY = curBalY;
    oldDirX = curDirX;
    oldDirY = curDirY;

    String retu = ret.toString();
    return retu;
  }
}
