package pong;

import javafx.scene.input.KeyCode;

public class Controll
{
  KeyCode keyDown;
  KeyCode keyUp;
  Beam left;
  Beam right;
  Bal bal;
  Score score;
  Message message;
  Pause pause;

  public Controll(Beam left, Beam right, Bal bal, Score score, Message message, Pause pause)
  {
    this.left = left;
    this.right = right;
    this.bal = bal;
    this.score = score;
    this.message = message;
    this.pause = pause;
  }

  public void setKey(KeyCode keyCode)
  {
    this.keyDown = keyCode;
    BeamChange();
  }

  public void setRelease(KeyCode keyCode)
  {
    switch (keyCode)
    {
      case UP:
      case DOWN:
        right.stopY();
        break;

      case W:
      case S:
        left.stopY();
        break;

      default:
        break;
    }
  }

  private void BeamChange()
  {
    switch (keyDown)
    {
      case UP:
        right.startY(-5);
        break;

      case DOWN:
        right.startY(5);
        break;

      case W:
        left.startY(-5);
        break;

      case S:
        left.startY(5);
        break;

      case SPACE:
        score.restart();
        bal.nextGame();
        left.restart();
        right.restart();
        System.gc();
        break;

      case ESCAPE:
      case P:
        pause.setPause();
        break;

      default:
        break;
    }
  }
}
