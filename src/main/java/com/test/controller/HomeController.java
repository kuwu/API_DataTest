
package com.test.controller;

/**
 * Created by Kuwu on 7/24/17.
 */

import jdk.internal.util.xml.impl.Input;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;


@Controller
public class HomeController {

  @RequestMapping(value = {"/", "getweatherjson"}, method = RequestMethod.GET)

  public ModelAndView helloWorld(Model model) {

    String prodCenter = "";
    try {

      // the HTTPClient Interface represents the contract for the HTTP Request execution
      HttpClient http = HttpClientBuilder.create().build();

      //HTTPHost holds the variables needed for the connection
      // default port for http is 80
      // default port for https is 443
      HttpHost host = new HttpHost("data.cityofchicago.org", 443, "https");

      // HttpGet retrieves the info identified by the request URI (in the form of an entity)
      HttpGet getPage = new HttpGet("/resource/f7f2-ggz5.json");

      // execute the http request and pull the response
      HttpResponse resp = http.execute(host, getPage);

      String jsonString = EntityUtils.toString(resp.getEntity());

      // assign the returned result to a json object
      int i = 0;

      JSONArray json = new JSONArray(jsonString);
      //JSONObject json = new JSONObject(jsonString);

      //prodCenter = json.get("productionCenter").toString();

      // this is for me as a developer to identify that my API is working
      System.out.println("Response code: " + resp.getStatusLine().getStatusCode());

      // String to hold data for our loop once we return the jason array
      String text = "";

      // create a json array to hold the data in the "text" array node
      // also think of this as json array has an array of text nested inside the data object
      //JSONArray ar = json.getJSONArray("JSON");
      JSONObject obj = json.getJSONObject(1);

      text = obj.getString("zip");
      //text += obj.getJSONObject("id");

      // loop through json array
//      for (i = 0; i < ar.length(); i++) {
//        text += ("<h6>" + ar.getString(i) + "<h6>");
//      }

      model.addAttribute("jsonArray", text);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return new
        ModelAndView("welcome", "message", prodCenter);

  }

  @RequestMapping(value = "getweatherxml", method = RequestMethod.GET)
  public String getXML(Model model) {

    try {
      // the HTTPClient Interface represents the contract for the HTTP Request execution
      HttpClient http = HttpClientBuilder.create().build();

      //HTTPHost holds the variables needed for the connection
      // default port for http is 80
      // default port for https is 443
      HttpHost host = new HttpHost("forecast.weather.gov", 80, "http");

      // HttpGet retrieves the info identified by the request URI (in the form of an entity)
      HttpGet getPage = new HttpGet("/MapClick.php?lat=42.331427&lon=-83.045754&FcstType=xml");

      // execute the http request and pull the response
      HttpResponse resp = http.execute(host, getPage);

      String xmlString = EntityUtils.toString(resp.getEntity());

      // factory is enabling our application to obtain a parser for the XML DOM
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

      DocumentBuilder db = factory.newDocumentBuilder();

      InputSource inStream = new InputSource();

      inStream.setCharacterStream(new StringReader(xmlString));

      Document doc = db.parse(inStream);

      String result = "";
      String weatherForecast = "";

      NodeList nl = doc.getElementsByTagName("text");

      for (int i = 0; i < nl.getLength(); i++) {

        // cast nl as an element of the DOM -- this is used for xml processing
        org.w3c.dom.Element nameElement = (org.w3c.dom.Element) nl.item(i);

        weatherForecast = nameElement.getFirstChild().getNodeValue().trim();

        result += ("<h6>" + weatherForecast + "</h6>");

      }

      model.addAttribute("xmlPageData", result);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }


    return "xmlData";
  }



}