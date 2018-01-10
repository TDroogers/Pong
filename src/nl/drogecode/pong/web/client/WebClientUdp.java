package nl.drogecode.pong.web.client;

import java.io.*;
import java.net.*;

public class WebClientUdp
{
  private MulticastSocket socket;
  private InetAddress group;

  private byte[] buf;

  public WebClientUdp() throws IOException
  {
    socket = new MulticastSocket(4446);
    group = InetAddress.getByName("230.0.0.1");
    socket.joinGroup(group);
    socket.setSoTimeout(5000);
  }

  public String getBroadcast() throws IOException
  {
    String received = "";
    try
    {
      DatagramPacket packet;

      buf = new byte[256];
      packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet);

      received = new String(packet.getData());

      socket.leaveGroup(group);
      socket.close();
    }
    catch (SocketTimeoutException e)
    {
      System.out.println("Timeout: " + e);
    }
    return received;
  }

  public void close()
  {
    socket.close();
  }
}
