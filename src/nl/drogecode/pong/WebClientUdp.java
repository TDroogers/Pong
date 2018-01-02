package nl.drogecode.pong;

import java.io.*;
import java.net.*;

public class WebClientUdp
{
  private DatagramSocket socket;
  private InetAddress address;

  private byte[] buf;

  public WebClientUdp() throws SocketException, UnknownHostException
  {
    socket = new DatagramSocket();
    address = InetAddress.getByName("192.168.1.0");
  }

  public String sendEcho(String msg) throws IOException
  {
    buf = msg.getBytes();
    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
    socket.send(packet);
    packet = new DatagramPacket(buf, buf.length);
    socket.receive(packet);
    String received = new String(packet.getData(), 0, packet.getLength());
    System.out.println(packet);
    return received;
  }

  public void close()
  {
    socket.close();
  }
}
