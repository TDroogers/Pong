package nl.drogecode.pong;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class WebServerUdp extends Thread {
  
  private DatagramSocket socket;
  private boolean running;
  private byte[] buf = new byte[256];

  public WebServerUdp() throws SocketException {
      socket = new DatagramSocket(4445);
  }

  @Override public void run() {
      running = true;

      while (running) {
        try {
          DatagramPacket packet 
            = new DatagramPacket(buf, buf.length);
          socket.receive(packet);
          InetAddress address = packet.getAddress();
          int port = packet.getPort();
          packet = new DatagramPacket(buf, buf.length, address, port);
          String received 
            = new String(packet.getData(), 0, packet.getLength());
          //System.out.println(received);
          if (received.equals("end")) {
              running = false;
              continue;
          }
          String msg = "terug weg";
          System.out.println(msg);
          buf = msg.getBytes();
          packet = new DatagramPacket(buf, buf.length, address, port);
          
          socket.send(packet);
        }
        catch(Exception e)
        {
          System.out.println("UDP server failed: " + e);
        }
      }
      socket.close();
  }
}