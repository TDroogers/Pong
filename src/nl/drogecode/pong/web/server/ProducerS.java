package nl.drogecode.pong.web.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;

import javafx.application.Platform;
import nl.drogecode.pong.objects.MovableObjects;

public class ProducerS extends Thread
{
  private Socket socket = null;
  private MovableObjects movable;
  String inputLine, outputLine;
  private long cal = Calendar.getInstance().getTimeInMillis();
  private long calMax = 0;
  private byte oldScoreL, oldScoreR, curScoreL, curScoreR;
  private double oldClientY, oldSerY, oldBalX, oldBalY, oldDirX, oldDirY;
  private double curSerY, curBalX, curBalY, curDirX, curDirY;

  public ProducerS(Socket socket, MovableObjects movable)
  {
    super("KKMultiServerThread " + socket.getLocalPort());
    this.socket = socket;
    this.movable = movable;
    oldClientY = movable.getBeamRightY();
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
    try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));)
    {
      outputLine = toClintFullString();
      out.println("hello/" + outputLine);

      for (;;)
      {
        inputLine = in.readLine();
        readByte(inputLine);
        outputLine = toClientString();
        out.println(outputLine);

        long calnieuw = Calendar.getInstance().getTimeInMillis();
        long difference = calnieuw - cal;
        cal = calnieuw;

        if (difference > calMax)
        {
          calMax = difference;
          System.out.println(calMax);
        }
        System.out.println(calMax + " : " + difference);
        
        yield();
        
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
  private void readByte(String inputLine)
  {
    try
    {
      double result = Double.parseDouble(inputLine);
      result += oldClientY;
      updateBeamClientY(result);
      oldClientY = result;
    }
    catch (Exception e)
    {
      System.out.println("not a byte: " + e);
    }
  }

  private void updateBeamClientY(double newY)
  {
    Platform.runLater(new Runnable()
    {
      @Override public void run()
      {
        movable.setBeamRightY(newY);
      }
    });
  }

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
