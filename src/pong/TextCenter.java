package pong;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class TextCenter extends Text
{
  private Stage stage;

  protected void setStage(Stage stage)
  {
    this.stage = stage;
  }

  protected void setProjectDefault()
  {
    setFont(new Font(20));
    setTextAlignment(TextAlignment.CENTER);
    setCenter();
  }

  protected void setCenter()
  {
    setX((stage.getScene().getWidth() / 2) - (getLayoutBounds().getWidth() / 2));
  }
}
