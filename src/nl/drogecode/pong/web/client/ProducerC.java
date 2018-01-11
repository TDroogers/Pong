package nl.drogecode.pong.web.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import nl.drogecode.pong.Sleeper;
import nl.drogecode.pong.objects.MovableObjects;

public class ProducerC extends Thread
{
  Socket socket;
  MovableObjects movable;

  private double previus, currentY;
  private String ret;

  public ProducerC(Socket socket, MovableObjects movable)
  {
    this.socket = socket;
    this.movable = movable;
  }

  @Override public void run()
  {
    Sleeper sleep = new Sleeper();
    previus = movable.getBeamRightY();

    try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);)
    {
      for (;;)
      {
        out.println(toServerByte());
        yield();
        sleep.sleeper(Long.MAX_VALUE);
      }
    }

    catch (SocketException e)
    {
      System.err.println("Connection lost with server: " + e);
      movable.setPlayer("co-op");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private String toServerByte()
  {
    currentY = movable.getBeamRightY();
    ret = String.valueOf(currentY - previus);
    ret = ret.concat("/").concat(String.valueOf(movable.getChangeR()));
    previus = currentY;
    return ret;
  }
}
