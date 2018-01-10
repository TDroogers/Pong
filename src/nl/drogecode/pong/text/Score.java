/*
 * Shows and updates the score on top of the screen.
 */
package nl.drogecode.pong.text;

import javafx.stage.Stage;
import nl.drogecode.pong.Settings;

public class Score extends TextCenter
{
  private int scoreRight = 0;
  private int scoreLeft = 0;
  private int winScore;

  public Score(Stage stage, Settings settings)
  {
    winScore = settings.getWinScore();

    setStage(stage);
    setText("press [SPACE] to start!\nThe first hitting " + winScore + " wins?");
    setProjectDefault();
    setY(50);
  }

  public void restart()
  {
    scoreRight = 0;
    scoreLeft = 0;
    updateScore();
    setCenter();
  }

  public boolean setScoreRight()
  {
    scoreRight++;
    if (doWeHaveAWinner(scoreRight))
    {
      return false;
    }
    updateScore();
    return true;
  }

  public boolean setScoreLeft()
  {
    scoreLeft++;
    if (doWeHaveAWinner(scoreLeft))
    {
      return false;
    }
    updateScore();
    return true;
  }

  public void setFullText(String text)
  {
    setText(text);
    setCenter();
  }

  private void updateScore()
  {
    setText(scoreRight + " | " + scoreLeft);
  }

  private boolean doWeHaveAWinner(int score)
  {
    if (score >= winScore)
    {
      setText(scoreRight + " | " + scoreLeft + "\n" + "We have a winner" + "\n" + "press [SPACE] to play again!");
      setCenter();
      return true;
    }
    return false;
  }
}
