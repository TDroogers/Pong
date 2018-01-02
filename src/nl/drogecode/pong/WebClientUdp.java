package nl.drogecode.pong;

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
  }

  public String getBroadcast() throws IOException
  {
    DatagramPacket packet;

    buf = new byte[256];
    packet = new DatagramPacket(buf, buf.length);
    socket.receive(packet);

    String received = new String(packet.getData());

    socket.leaveGroup(group);
    socket.close();
    return received;
  }

  public void close()
  {
    socket.close();
  }
}
