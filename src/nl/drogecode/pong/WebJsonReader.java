/*
 * 
 * Thanks:
 * 
 * https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
 * 
 * tested with: {"0":{"idUser":"1","name":"Admin"},"1":{"idUser":"2","name":"John Doe"},"2":{"idUser":"3","name":"Taco Droogers"},"3":{"idUser":"4","name":"test"}}
 * as result from localhost.
 * 
 */

package nl.drogecode.pong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class WebJsonReader
{
  
  private static String url = Settings.getUrl() + "?load=api&api=java_pong&type=json";

  public WebJsonReader()
  {
    try
    {
      testertje();
    }
    catch (IOException | JSONException e1)
    {
      e1.printStackTrace();
    }
  }

  public static void testertje() throws IOException, JSONException
  {
    JSONObject json = readJsonFromUrl(url);
    System.out.println(json.toString());
    System.out.println(((JSONObject) json.get("0")).get("idUser"));

    Iterator<?> keys = json.keys();

    while (keys.hasNext())
    {
      String key = (String) keys.next();
      if (json.get(key) instanceof JSONObject)
      {
        System.out.println(json.get(key));
      }
    }
  }

  private static String readAll(Reader rd) throws IOException
  {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1)
    {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException
  {
    InputStream is = new URL(url).openStream();
    try
    {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    }
    finally
    {
      is.close();
    }
  }
}
