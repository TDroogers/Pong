package nl.drogecode.pong.web.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javafx.application.Platform;
import nl.drogecode.pong.objects.MovableObjects;

public class ConsumerS extends Thread
{
  private Socket socket = null;
  private MovableObjects movable;
  String inputLine, outputLine;
  private double oldClientY;
  
  public ConsumerS (Socket socket, MovableObjects movable)
  {
    this.socket = socket;
    this.movable = movable;
    oldClientY = movable.getBeamRightY();
  }
  
  @Override public void run()
  {
    try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));)
    {
      for(;;)
      {
        inputLine = in.readLine();
        readString(inputLine);
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
  
  
  private void readString(String inputLine)
  {
    try
    {
      String[] ary = inputLine.split("/");
      double result = Double.parseDouble(ary[0]);
      int beamYChange = Integer.parseInt(ary[1]);
      result += oldClientY;
      updateScreen(result, beamYChange);
      oldClientY = result;
    }
    catch (Exception e)
    {
      System.out.println("not a byte: " + e);
    }
  }
  
  private void updateScreen(double newY, int beamYChange)
  {
    Platform.runLater(new Runnable()
    {
      @Override public void run()
      {
        movable.setBeamRightY(newY, beamYChange);
      }
    });
  }
}
