package nl.drogecode.pong;

import java.util.Timer;
import java.util.TimerTask;

public class WakeUp
{
  Timer timer;
  
  public WakeUp()
  {
    timer = new Timer();
  }
  
  public synchronized void setNewThread(Thread thread)
  {
    timer.schedule(new runningThreadWaker(thread), 1000);
  }
  
  private class runningThreadWaker extends TimerTask
  {
    private Thread thread;
    private runningThreadWaker(Thread thread)
    {
      this.thread = thread;
    }

    @Override public void run()
    {
      timer.schedule(new runningThreadWaker(thread), 1000);
      thread.interrupt();
      System.out.println("wake UP!");
    }
    
  }
}
