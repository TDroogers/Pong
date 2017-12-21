/*
 * 
 * Thanks:
 * 
 * https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
 * 
 * tested with: {"id":"93","dbResult0":{"0":{"ID":"93","Name":"1642681752","DateTime":"2017-12-21 13:18:20","ip":"127.0.0.1"},"1":{"ID":"94","Name":"111100000","DateTime":"2017-12-21 13:18:17","ip":"::1"},"2":{"ID":"95","Name":"111100000","DateTime":"2017-12-21 13:18:17","ip":"::1"},"3":{"ID":"96","Name":"111100000","DateTime":"2017-12-21 13:18:17","ip":"::1"},"4":{"ID":"97","Name":"111100000","DateTime":"2017-12-21 13:18:18","ip":"::1"}}}
 * as result from localhost.
 * 
 */

package nl.drogecode.pong;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader
{

  private JSONObject json;
  private String id = "";

  public JsonReader(String webResult)
  {
    try
    {
      json = new JSONObject(webResult);
    }
    catch (JSONException e1)
    {
      e1.printStackTrace();
    }
  }

  public void getPartAsString() throws JSONException
  {
    id = (String) json.get("id");
    System.out.println((id));
    Iterator<?> keys = json.keys();

    loopInsideLoop(keys, json);
  }

  public String getId()
  {
    return id;
  }

  private void loopInsideLoop(Iterator<?> keys, JSONObject json) throws JSONException
  {
    while (keys.hasNext())
    {
      String part = (String) keys.next();
      if ((json.get(part) instanceof JSONObject))
      {
        System.out.println(json.get(part));
        JSONObject nextLoop = (JSONObject) json.get(part);
        Iterator<?> key = nextLoop.keys();

        loopInsideLoop(key, nextLoop);
      }
    }
  }
}
