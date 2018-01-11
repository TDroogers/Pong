package nl.drogecode.pong;

import java.util.Timer;
import java.util.TimerTask;

public class WakeUp
{
  private Timer timer;
  private final int SLEEPTIME = 100;
  
  public WakeUp()
  {
    timer = new Timer();
  }
  
  public synchronized void setNewThread(Thread thread)
  {
    timer.schedule(new runningThreadWaker(thread), SLEEPTIME);
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
      timer.schedule(new runningThreadWaker(thread), SLEEPTIME);
      thread.interrupt();
    }
    
  }
}
