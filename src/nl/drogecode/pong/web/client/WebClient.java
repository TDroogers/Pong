/*
 * from: https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
 */
package nl.drogecode.pong.web.client;

import java.io.IOException;
import java.net.Socket;

import nl.drogecode.pong.WakeUp;
import nl.drogecode.pong.objects.MovableObjects;

public class WebClient
{
  private MovableObjects movable;
  String hostName;

  public boolean client(MovableObjects movable) throws IOException
  {
    this.movable = movable;

    if (!searchForServer())
    {
      return false;
    }

    makeConnectionWithSever();
    return true;
  }

  private boolean searchForServer() throws IOException
  {
    WebClientUdp udp = new WebClientUdp();
    hostName = udp.getBroadcast();

    if (hostName.equals(""))
    {
      return false;
    }
    System.out.println(hostName);
    return true;
  }

  private boolean makeConnectionWithSever() throws IOException
  {
    int portNumber = 2315;

    Socket socket = new Socket(hostName, portNumber);
    movable.setPlayer("client");
    startNewThread(socket);
    return true;
  }

  private void startNewThread(Socket socket) throws IOException
  {
    Thread producer = new ProducerC(socket, movable);
    Thread consumer = new ConsumerC(socket, movable);
    producer.start();
    consumer.start();

    WakeUp wake = new WakeUp();
    wake.setNewThread(producer);
  }
}
