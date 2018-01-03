package nl.drogecode.pong;

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
  String side = "co up";

  public Controll(Beam left, Beam right, Bal bal, Score score, Message message, Pause pause)
  {
    this.left = left;
    this.right = right;
    this.bal = bal;
    this.score = score;
    this.message = message;
    this.pause = pause;
  }

  public void setPlayer(String side)
  {
    this.side = side;
  }

  public void setKey(KeyCode keyCode)
  {
    this.keyDown = keyCode;
    switch (side)
    {
      case "co up":
        beamStart();
        break;

      case "client":
        beamStartClient();
        break;

      case "server":
        beamStartServer();
        break;
    }
  }

  public void setRelease(KeyCode keyCode)
  {
    this.keyUp = keyCode;
    switch (side)
    {
      case "co up":
        beamStop();
        break;

      case "client":
        beamStopClient();
        break;

      case "server":
        beamStopServer();
        break;
    }
  }
  
  /*
   * ==============================================================
   * 
   * Private function's
   * 
   * ==============================================================
   */

  private void beamStop()
  {
    switch (keyUp)
    {
      case UP:
        right.stopY(-1);
        break;

      case DOWN:
        right.stopY(1);
        break;

      case W:
        left.stopY(-1);
        break;

      case S:
        left.stopY(1);
        break;

      default:
        break;
    }
  }

  private void beamStopClient()
  {
    switch (keyUp)
    {
      case UP:
        right.stopY(-1);
        break;

      case DOWN:
        right.stopY(1);
        break;

      default:
        break;
    }
  }

  private void beamStopServer()
  {

    switch (keyUp)
    {
      case UP:
        left.stopY(-1);
        break;

      case DOWN:
        left.stopY(1);
        break;

      default:
        break;
    }
  }

  private void beamStart()
  {
    switch (keyDown)
    {
      case UP:
        right.startY(-1);
        break;

      case DOWN:
        right.startY(1);
        break;

      case W:
        left.startY(-1);
        break;

      case S:
        left.startY(1);
        break;

      case SPACE:
        restart();
        break;

      case ESCAPE:
      case P:
        pause.setPause();
        break;

      default:
        break;
    }
  }

  private void beamStartClient()
  {
    switch (keyDown)
    {
      case UP:
        right.startY(-1);
        break;

      case DOWN:
        right.startY(1);
        break;

      // client can not restart the game.

      default:
        break;
    }
  }

  private void beamStartServer()
  {
    switch (keyDown)
    {
      case UP:
        left.startY(-1);
        break;

      case DOWN:
        left.startY(1);
        break;

      case SPACE:
        restart();
        break;

      default:
        break;
    }
  }

  private void restart()
  {
    score.restart();
    bal.nextGame();
    left.restart();
    right.restart();
    System.gc();
  }
}
