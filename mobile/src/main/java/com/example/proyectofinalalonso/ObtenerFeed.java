package com.example.proyectofinalalonso;

import android.os.AsyncTask;
import android.text.Html;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.R.attr.description;

/**
 * Created by Alonso on 13/09/2017.
 */

public class ObtenerFeed extends AsyncTask{
    URL url;
    ArrayList<String> headlines = new ArrayList();
    ArrayList<String> links = new ArrayList();
    ArrayList<String> description = new ArrayList();
    String rss = "";
    public ObtenerFeed(String rss)
    {
        this.rss = rss;
    }

    //Getters
    public ArrayList<String> getHeadlines ()
    {
        return headlines;
    }

    public ArrayList<String> getLinks ()
    {
        return links;
    }

    public ArrayList<String> getDescription ()
    {
        return description;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        // Initializing instance variables
        try {
            url = new URL(rss);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(getInputStream(url), "UTF_8");

            boolean insideItem = false;

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {

                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;
                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        if (insideItem)
                            headlines.add(xpp.nextText()); //extract the headline
                    } else if (xpp.getName().equalsIgnoreCase("link")) {
                        if (insideItem)
                            links.add(xpp.nextText()); //extract the link of article
                    }else if (xpp.getName().equalsIgnoreCase("description")) {
                        if (insideItem)
                            //descripcion=removeHtmlTags(xpp.nextText());
                            description.add(Html.fromHtml(xpp.nextText()).toString()); //extract the description of the article
                    }

                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                    insideItem = false;
                }

                eventType = xpp.next(); //move to next element
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return headlines;
    }

    @Override
    protected void onPreExecute(){}

    @Override
    protected void onPostExecute(Object o) {

    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

}
