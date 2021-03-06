package nl.drogecode.pong.text;

import javafx.stage.Stage;

public class Message extends TextCenter
{
  public Message(Stage stage)
  {
    setStage(stage);
    setY(stage.getScene().getHeight() / 2);
    setProjectDefault();
  }

  public void messageSwitch(boolean pause)
  {
    if (pause)
    {
      messageOn();
    }
    else
    {
      messageOff();
    }
  }

  public void setDifferentMessage(String mes)
  {
    setText(mes);
    setCenter();
  }

  private void messageOn()
  {
    setText("pause");
    setCenter();
  }

  private void messageOff()
  {
    setText("");
  }
}
