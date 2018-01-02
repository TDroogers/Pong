package nl.drogecode.pong;

import java.net.*;
import java.util.Map;

import org.json.JSONException;

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
      String inputLine, outputLine;
      WebServerProtocol kkp = new WebServerProtocol();
      outputLine = kkp.processInput(null);
      out.println(outputLine);

      while ((inputLine = in.readLine()) != null)
      {
        System.out.println(inputLine);
        readJson(inputLine);
        outputLine = kkp.processInput(inputLine);
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
  
  private void readJson(String inputLine)
  {
    try
    {
      WebJsonReader reader = new WebJsonReader(inputLine);
      mss = reader.getPartAsMap();

      movable.setBeamRightX(Double.parseDouble(mss.get("beamRightX")));
      movable.setBeamRightY(Double.parseDouble(mss.get("beamRightY")));
      System.out.println(mss.get("beamRightX"));
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
  }
}
