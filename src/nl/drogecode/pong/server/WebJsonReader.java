/*
 * 
 * Thanks: https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
 * 
 */

package nl.drogecode.pong.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class WebJsonReader
{

  private JSONObject json;
  private String id = "";
  Map<String,String> mss;

  public void setString(String webResult)
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

  public Map<String,String> getPartAsMap() throws JSONException
  {
    mss = new HashMap<String,String>();
    Iterator<?> keys = json.keys();

    loopInsideLoop(keys, json);
    
    return mss;
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
        /*
         * This part won't be reached in current version, however it is fun to keep.
         */
        JSONObject nextLoop = (JSONObject) json.get(part);
        Iterator<?> key = nextLoop.keys();

        loopInsideLoop(key, nextLoop);
      }
      else
      {
        mss.put(part, (String) json.get(part));
      }
    }
  }
}
