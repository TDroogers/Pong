/*
 * https://www.javagists.com/http2-client-java-9
 * 
 * from: https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
 *       https://docs.oracle.com/javase/tutorial/networking/datagrams/broadcasting.html
 */
package nl.drogecode.pong;

import java.net.*;
import java.io.*;

public class WebServer
{
  private static int PORTNUMBER = 2315;
  MovableObjects movable;

  public void server(MovableObjects movable) throws IOException
  {
    WebServerUdp serUdp = new WebServerUdp();
    this.movable = movable;
    serUdp.start();
    
    boolean listening = true;
    System.out.println(InetAddress.getLocalHost());
    try (ServerSocket serverSocket = new ServerSocket(PORTNUMBER))
    {
      while (listening)
      {
        new WebServerThread(serverSocket.accept(), movable).start();
        serUdp.stopUdp();
        listening = false;
        movable.setPlayer("server");
      }
    }
    catch (IOException e)
    {
      System.err.println("Could not listen on port " + PORTNUMBER);
      System.exit(-1);
    }
  }
}
