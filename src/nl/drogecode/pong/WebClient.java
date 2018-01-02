/*
 * from: https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
 */
package nl.drogecode.pong;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class WebClient
{
  MovableObjects movable;

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
        System.out.println("Server: " + fromServer);
        if (fromServer.equals("Bye."))
          break;


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

      //System.out.println(j);
    }
    catch(Exception e)
    {
      System.out.println("error in toServerJsonEncode() in WebClient: " + e);
    }
    return j;
  }
}
