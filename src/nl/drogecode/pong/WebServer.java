/*
 * https://www.javagists.com/http2-client-java-9
 * 
 * from: https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
 */
package nl.drogecode.pong;

import java.net.*;
import java.io.*;

public class WebServer
{
  public static void main(String[] args) throws IOException
  {

    int portNumber = 2315;

    try (ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket clientSocket = serverSocket.accept();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));)
    {

      String inputLine, outputLine;

      // Initiate conversation with client
      WebServerProtocol kkp = new WebServerProtocol();
      outputLine = kkp.processInput(null);
      out.println(outputLine);

      while ((inputLine = in.readLine()) != null)
      {
        outputLine = kkp.processInput(inputLine);
        out.println(outputLine);
        if (outputLine.equals("Bye."))
          break;
      }
    }
    catch (IOException e)
    {
      System.out
          .println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
      System.out.println(e.getMessage());
    }
  }
}
