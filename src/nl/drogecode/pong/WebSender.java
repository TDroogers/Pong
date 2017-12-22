/*
 * 
 * test url for browser: http://localhost/herbouw/?load=api&api=java_pong&type=json&step=hello&name=111100000
 * 
 */

package nl.drogecode.pong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class WebSender
{
  private String randomName, randomPoort;
  private String data;
  private String id = "";
  private boolean hello = false;

  public WebSender()
  {
    randomName = ((Integer) (int) (Math.random() * 2147483647 + 1)).toString();
    randomPoort = ((Integer) (int) (Math.random() * 50 + 2200)).toString();
    try
    {
      data = URLEncoder.encode("load", "UTF-8") + "=" + URLEncoder.encode("api", "UTF-8");
      data += "&" + URLEncoder.encode("api", "UTF-8") + "=" + URLEncoder.encode("java_pong", "UTF-8");
      data += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8");
      data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(randomName, "UTF-8");
      data += "&" + URLEncoder.encode("poort", "UTF-8") + "=" + URLEncoder.encode(randomPoort, "UTF-8");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  protected void connect()
  {
    String dataExtend = "";
    if (!hello)
    {
      dataExtend = firstContact();
      hello = true;
    }
    else
    {
      dataExtend = stillOnline();
    }
    String webResult;
    try
    {
      webResult = sender(dataExtend);
      WebJsonReader reader = new WebJsonReader(webResult);
      reader.getPartAsString();
      id = reader.getId();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private String firstContact()
  {
    String dataExtend = "";
    try
    {
      dataExtend += "&" + URLEncoder.encode("step", "UTF-8") + "=" + URLEncoder.encode("hello", "UTF-8");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return dataExtend;
  }

  private String stillOnline()
  {
    String dataExtend = "";
    try
    {
      dataExtend += "&" + URLEncoder.encode("step", "UTF-8") + "=" + URLEncoder.encode("ImBack", "UTF-8");
      dataExtend += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return dataExtend;
  }

  private String sender(String dataExtend) throws IOException
  {

    String url = Settings.getUrl();

    String dataSend = data + dataExtend;

    URL urll = new URL(url);
    URLConnection conn = urll.openConnection();
    conn.setDoOutput(true);
    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
    wr.write(dataSend);
    wr.flush();

    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String webResult, Result = null;
    while ((webResult = rd.readLine()) != null)
    {
      Result = webResult;
    }
    wr.close();
    rd.close();
    return Result;
  }
}
