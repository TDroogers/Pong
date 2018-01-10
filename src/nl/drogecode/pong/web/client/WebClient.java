/*
 * from: https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
 */
package nl.drogecode.pong.web.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Platform;
import nl.drogecode.pong.Sleeper;
import nl.drogecode.pong.WakeUp;
import nl.drogecode.pong.objects.MovableObjects;

public class WebClient
{
  private MovableObjects movable;
  private double previus, currentY, oldSerY, oldBalX, oldBalY, oldDirX, oldDirY, curSerY, curBalX, curBalY, curDirX,
      curDirY;
  private int scoreL, scoreR;
  private byte ret;
  private boolean scored;

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
      startNewThread(out, in);

      for(;;)
      {

      }
    }
    catch (UnknownHostException e)
    {
      System.err.println("Don't know about host " + hostName);
      movable.setPlayer("co-op");
    }
    catch (IOException e)
    {
      System.err.println("Couldn't get I/O for the connection to " + hostName);
      movable.setPlayer("co-op");
    }
    return true;
  }
  
  private void startNewThread(PrintWriter out, BufferedReader in) throws IOException
  {
    Thread producer = new ProducerC(out, movable);
    Thread consumer = new ConsumerC(in, movable);
    producer.start();
    consumer.start();
    
    WakeUp wake = new WakeUp();
    wake.setNewThread(producer);
  }

  /*
   * data efficient version
   */

}
