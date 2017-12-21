module nl.drogecode.pong
{

  // https://docs.oracle.com/javase/9/docs/api/overview-summary.html

  requires java.base;

  requires javafx.base;
  requires javafx.controls;
  requires javafx.fxml;
  requires transitive javafx.graphics;
  requires javafx.web;

  requires java.json;

  exports nl.drogecode.pong;
}
