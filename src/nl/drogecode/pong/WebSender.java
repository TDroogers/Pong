package nl.drogecode.pong;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class WebSender
{
  public static void main(String[] argv) throws Exception
  {
    String data = URLEncoder.encode("load", "UTF-8") + "=" + URLEncoder.encode("api", "UTF-8");
    data += "&" + URLEncoder.encode("api", "UTF-8") + "=" + URLEncoder.encode("api_tester", "UTF-8");

    String url = Settings.getUrl();

    URL urll = new URL(url);
    URLConnection conn = urll.openConnection();
    conn.setDoOutput(true);
    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
    wr.write(data);
    wr.flush();

    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String line;
    while ((line = rd.readLine()) != null)
    {
      System.out.println(line);
    }
    wr.close();
    rd.close();
  }
}
