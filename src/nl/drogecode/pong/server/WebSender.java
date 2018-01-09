/*
 * 
 * test url for browser: http://localhost/herbouw/?load=api&api=java_pong&type=json&step=hello&name=111100000
 * 
 */

package nl.drogecode.pong.server;

import java.io.IOException;

import javafx.concurrent.Task;
import nl.drogecode.pong.MovableObjects;

public class WebSender
{

  private WebClient client;
  private WebServer server;
  private MovableObjects movable;

  public WebSender()
  {
    client = new WebClient();
    server = new WebServer();
  }

  public void connect()
  {
    Task<Void> task = new Task<Void>()
    {
      @Override protected Void call() throws Exception
      {
        insideThread();
        return null;
      }
    };
    Thread th = new Thread(task);
    th.setDaemon(true);
    th.start();
  }

  public void setControll(MovableObjects movable)
  {
    this.movable = movable;
  }

  private void insideThread()
  {
    try
    {
      if (!client.client(movable))
      {
        server.server(movable);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
