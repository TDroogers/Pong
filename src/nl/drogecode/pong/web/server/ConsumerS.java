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
        readByte(inputLine);
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
}
