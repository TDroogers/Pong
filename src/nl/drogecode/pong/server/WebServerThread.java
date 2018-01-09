package nl.drogecode.pong.server;

import java.net.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import javafx.application.Platform;
import nl.drogecode.pong.MovableObjects;

import java.io.*;

public class WebServerThread extends Thread
{
  private Socket socket = null;
  private MovableObjects movable;
  private Map<String, String> mss;
  private WebJsonReader reader = new WebJsonReader();
  private long cal = Calendar.getInstance().getTimeInMillis();
  private long calMax = 0;

  public WebServerThread(Socket socket, MovableObjects movable)
  {
    super("KKMultiServerThread " + socket.getLocalPort());
    this.socket = socket;
    this.movable = movable;
  }

  @Override public void run()
  {
    try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));)
    {
      String inputLine;
      JSONObject outputLine;
      out.println("hello");

      while ((inputLine = in.readLine()) != null)
      {
        readJson(inputLine);
        outputLine = toServerJsonEncode();
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

      }
    }
    catch (IOException e)
    {
      System.out.println("connection lost " + e);
      movable.setPlayer("co up");
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

  private JSONObject toServerJsonEncode()
  {
    JSONObject j = null;
    try
    {
      Map<String, String> mss = new HashMap<String, String>();
      mss.put("beamLeftY", String.valueOf(movable.getBeamLeftY()));
      mss.put("balX", String.valueOf(movable.getBalX()));
      mss.put("balY", String.valueOf(movable.getBalY()));
      mss.put("balDirX", String.valueOf(movable.getBalDirX()));
      mss.put("balDirY", String.valueOf(movable.getBalDirY()));
      mss.put("score", String.valueOf(movable.getScore()));

      j = new JSONObject(mss);
    }
    catch (Exception e)
    {
      System.out.println("error in toServerJsonEncode() in WebServerThread: " + e);
    }
    return j;
  }

  private void readJson(String inputLine)
  {
    try
    {
      reader.setString(inputLine);
      mss = reader.getPartAsMap();

      Platform.runLater(new Runnable()
      {
        @Override public void run()
        {
          movable.setBeamRightY(Double.parseDouble(mss.get("beamRightY")));
        }
      });
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
  }
}
