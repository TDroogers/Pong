module nl.drogecode.pong_m
{

  // https://docs.oracle.com/javase/9/docs/api/overview-summary.html

  requires java.base;
  requires javafx.base;
  requires javafx.controls;
  requires javafx.fxml;
  requires transitive javafx.graphics;
  requires javafx.web;

  /*
   * imports from above the ragular java envirment.
   */
  requires java.json;

  /*
   * incubator modules are work in progress, so guarantee it the finall version will work as prommised.
   */
   //requires jdk.incubator.httpclient;
  // requires java.httpclient; // most likely it's future name.

  exports nl.drogecode.pong;
}
