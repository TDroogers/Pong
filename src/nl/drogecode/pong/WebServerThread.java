package nl.drogecode.pong;

import java.net.*;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class WebServerThread extends Thread
{
  private Socket socket = null;
  MovableObjects movable;
  Map<String,String> mss;

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
        //System.out.println(inputLine);
        readJson(inputLine);
        outputLine = toServerJsonEncode();
        out.println(outputLine);
        if (outputLine.equals("Bye"))
          break;
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
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
      Map<String,String> mss=new HashMap<String,String>();
      mss.put("beamLeftX", String.valueOf(movable.getBeamLeftX()));
      mss.put("beamLeftY", String.valueOf(movable.getBeamLeftY()));
      mss.put("balX", String.valueOf(movable.getBalX()));
      mss.put("balY", String.valueOf(movable.getBalY()));
      mss.put("score", String.valueOf(movable.getScore()));

      j=new JSONObject(mss);
    }
    catch(Exception e)
    {
      System.out.println("error in toServerJsonEncode() in WebClient: " + e);
    }
    return j;
  }
  
  private void readJson(String inputLine)
  {
    try
    {
      WebJsonReader reader = new WebJsonReader(inputLine);
      mss = reader.getPartAsMap();

      movable.setBeamRightX(Double.parseDouble(mss.get("beamRightX")));
      movable.setBeamRightY(Double.parseDouble(mss.get("beamRightY")));
      //System.out.println(mss.get("beamRightX"));
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
  }
}
