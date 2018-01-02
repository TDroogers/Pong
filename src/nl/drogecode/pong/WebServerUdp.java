package nl.drogecode.pong;

import java.net.*;

public class WebServerUdp extends Thread {
  
  private DatagramSocket socket;
  private volatile boolean running;
  private byte[] buf = new byte[256];

  public WebServerUdp() throws SocketException {
      socket = new DatagramSocket(4445);
  }

  @Override public void run() {
      running = true;

      while (running) {
        try {
          InetAddress address = InetAddress.getByName("230.0.0.1");
          int port = 4446;
          InetAddress me = InetAddress.getLocalHost();
          String msg = me.getHostAddress();
         
          buf = msg.getBytes();
          DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
          
          socket.send(packet);
          Sleeper sleep = new Sleeper();
          sleep.sleeper(100);
        }
        catch(Exception e)
        {
          System.out.println("UDP server failed: " + e);
        }
      }
      socket.close();
  }
  
  public void stopUdp()
  {
    running = false;
  }
}