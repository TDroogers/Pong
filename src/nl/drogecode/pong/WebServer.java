/*
 * https://www.javagists.com/http2-client-java-9
 * 
 * from: https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
 * https://systembash.com/a-simple-java-udp-server-and-udp-client/
 * http://www.codejava.net/java-se/networking/java-udp-client-server-program-example
 * http://www.baeldung.com/udp-in-java
 */
package nl.drogecode.pong;

import java.net.*;
import java.io.*;

public class WebServer
{
  private static int PORTNUMBER = 2315;

  public static void main(String[] args) throws IOException
  {
    new WebServerUdp().start();
    
    boolean listening = true;
    System.out.println(InetAddress.getLocalHost());
    try (ServerSocket serverSocket = new ServerSocket(PORTNUMBER))
    {
      while (listening)
      {
        new WebServerThread(serverSocket.accept()).start();
      }
    }
    catch (IOException e)
    {
      System.err.println("Could not listen on port " + PORTNUMBER);
      System.exit(-1);
    }
  }
}
