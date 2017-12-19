package nl.drogecode.pong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Server
{

  public static void main(String[] args)
  {
    String url = "http://localhost/herbouw/?load=api&api=java_pong&type=json";
    String xml = callURL(url);
    System.out.println("\nOutput: \n" + xml);
    
    //stringToXml(xml);
  }

  public static String callURL(String myURL)
  {
    System.out.println("Requeted URL:" + myURL);
    StringBuilder sb = new StringBuilder();
    URLConnection urlConn = null;
    InputStreamReader in = null;
    try
    {
      URL url = new URL(myURL);
      urlConn = url.openConnection();
      if (urlConn != null)
        urlConn.setReadTimeout(60 * 1000);
      if (urlConn != null && urlConn.getInputStream() != null)
      {
        in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
        BufferedReader bufferedReader = new BufferedReader(in);
        if (bufferedReader != null)
        {
          int cp;
          while ((cp = bufferedReader.read()) != -1)
          {
            sb.append((char) cp);
          }
          bufferedReader.close();
        }
      }
      in.close();
    }
    catch (Exception e)
    {
      throw new RuntimeException("Exception while calling URL:" + myURL, e);
    }

    return sb.toString();
  }
  
  public static void stringToXml(String xml)
  {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = null;
    try {
        db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
        try {
            Document doc = db.parse(is);
            String message = doc.getDocumentElement().getTextContent();
            System.out.println(message);
        } catch (SAXException e) {
            // handle SAXException
        } catch (IOException e) {
            // handle IOException
        }
    } catch (ParserConfigurationException e1) {
        // handle ParserConfigurationException
    }
  }
}
