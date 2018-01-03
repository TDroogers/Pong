/*
 * from: https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
 */
package nl.drogecode.pong;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class WebClient
{
  MovableObjects movable;
  Map<String,String> mss;

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
          //System.out.println("Server: " + fromServer);
          if (fromServer.equals("Bye."))
          {
            break;
          }
        }


        out.println(toServerJsonEncode());
        sleep.sleeper(20);
      }
    }
    catch (UnknownHostException e)
    {
      System.err.println("Don't know about host " + hostName);
      System.exit(1);
    }
    catch (IOException e)
    {
      System.err.println("Couldn't get I/O for the connection to " + hostName);
      System.exit(1);
    }
    return true;
  }

  private JSONObject toServerJsonEncode()
  {
    JSONObject j = null;
    try
    {
      Map<String,String> mss=new HashMap<String,String>();
      mss.put("beamRightX", String.valueOf(movable.getBeamRightX()));
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
      //System.out.println(fromServer);
      
      WebJsonReader reader = new WebJsonReader(fromServer);
      mss = reader.getPartAsMap();

      movable.setBeamLeftX(Double.parseDouble(mss.get("beamLeftX")));
      movable.setBeamLeftY(Double.parseDouble(mss.get("beamLeftY")));
      movable.setBalX(Double.parseDouble(mss.get("balX")));
      movable.setBalY(Double.parseDouble(mss.get("balY")));
      movable.setScore(mss.get("score"));
      //System.out.println(mss.get("beamLeftX"));
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
  }
}
