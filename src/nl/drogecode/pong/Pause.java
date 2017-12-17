package nl.drogecode.pong;

public class Pause
{
  private Message message;
  private boolean pause = false;

  public Pause(Message message)
  {
    this.message = message;
  }

  public void setPause()
  {
    pause = !pause;

    message.messageSwitch(pause);
  }

  public boolean getPause()
  {
    return pause;
  }
}
