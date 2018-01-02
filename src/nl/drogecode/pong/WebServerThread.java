package nl.drogecode.pong;

import java.net.*;
import java.io.*;

public class WebServerThread extends Thread
{
  private Socket socket = null;

  public WebServerThread(Socket socket)
  {
    super("KKMultiServerThread " + socket.getLocalPort());
    this.socket = socket;
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
}
