package pong;

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
    if (pause)
    {
      pause = false;
    }
    else
    {
      pause = true;
    }

    message.messageSwitch(pause);
  }

  public boolean getPause()
  {
    if (pause == true)
    {
      return true;
    }
    return false;
  }
}
