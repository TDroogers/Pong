/*
 * from: https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
 */
package nl.drogecode.pong.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import javafx.application.Platform;
import nl.drogecode.pong.MovableObjects;
import nl.drogecode.pong.Sleeper;

public class WebClient
{
  private MovableObjects movable;
  private Map<String,String> mss;
  private WebJsonReader reader = new WebJsonReader();

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
        if (fromServer.equals("hello"))
        {
          System.out.println("connection made");
        }
        else
        {
          readJson(fromServer);
        }


        out.println(toServerJsonEncode());
        sleep.sleeper(30);
      }
    }
    catch (UnknownHostException e)
    {
      System.err.println("Don't know about host " + hostName);
      movable.setPlayer("co up");
    }
    catch (IOException e)
    {
      System.err.println("Couldn't get I/O for the connection to " + hostName);
      movable.setPlayer("co up");
    }
    return true;
  }

  private JSONObject toServerJsonEncode()
  {
    JSONObject j = null;
    try
    {
      Map<String,String> mss=new HashMap<String,String>();
      mss.put("beamRightY", String.valueOf(movable.getBeamRightY()));

      j=new JSONObject(mss);
    }
    catch(Exception e)
    {
      System.out.println("error in toServerJsonEncode() in WebClient: " + e);
    }
    return j;
  }
  
  private void readJson(String fromServer)
  {
    try
    {
      reader.setString(fromServer);
      mss = reader.getPartAsMap();

      Platform.runLater(new Runnable()
      {
        @Override public void run()
        {
          movable.setBeamLeftY(Double.parseDouble(mss.get("beamLeftY")));
          movable.setBalX(Double.parseDouble(mss.get("balX")));
          movable.setBalY(Double.parseDouble(mss.get("balY")));
          movable.setBalDirY(Double.parseDouble(mss.get("balDirX")));
          movable.setBalDirY(Double.parseDouble(mss.get("balDirY")));
          movable.setScore(mss.get("score"));
        }
      });
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
  }
}
