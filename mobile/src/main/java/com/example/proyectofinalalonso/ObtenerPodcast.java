package com.example.proyectofinalalonso;

import android.app.ProgressDialog;
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
import static android.R.attr.y;

/**
 * Created by Alonso on 13/09/2017.
 */

public class ObtenerPodcast extends AsyncTask <Void, Integer, ArrayList<ArrayList<String>>> {
    URL url;
    ArrayList<String> ownImages = new ArrayList();
    ArrayList<String> links = new ArrayList();
    ArrayList<String> descriptions = new ArrayList();
    ArrayList<String> images = new ArrayList();
    ArrayList<String> durations = new ArrayList();
    ArrayList<String> titles = new ArrayList();
    String rss = "";
    ProgressDialog dialog;
    public AsyncResponse delegate = null;

    public ObtenerPodcast(String rss) {
        this.rss = rss;
    }

    public ObtenerPodcast(AsyncResponse delegate)
    {
        this.delegate = delegate;
    }

    //Getters
    public ArrayList<String> getOwnImages() {
        return ownImages;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public ArrayList<String> getDescription() {
        return descriptions;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public ArrayList<String> getDuration() {
        return durations;
    }

    public ArrayList<String> getTitle() {
        return titles;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Cargando datos, por favor espere");
        dialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer ... y){
        super.onProgressUpdate(y);
    }

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(Void ... a) {
        // Initializing instance variables

        String descripcion = "";
        Integer cont = 0;
        ArrayList<ArrayList<String>> outer = new ArrayList<ArrayList<String>>();
        try {
            url = new URL(rss);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();

            // We will get the XML from an input stream
            xpp.setInput(getInputStream(url), "UTF_8");

        /* We will parse the XML content looking for the "<title>" tag which appears inside the "<item>" tag.
         * However, we should take in consideration that the rss feed name also is enclosed in a "<title>" tag.
         * As we know, every feed begins with these lines: "<channel><title>Feed_Name</title>...."
         * so we should skip the "<title>" tag which is a child of "<channel>" tag,
         * and take in consideration only "<title>" tag which is a child of "<item>"
         *
         * In order to achieve this, we will make use of a boolean variable.
         */
            boolean insideItem = false;

            // Returns the type of current event: START_TAG, END_TAG, etc..
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT && cont<10) {
                if (eventType == XmlPullParser.START_TAG) {
                 /*   if (xpp.getName().equalsIgnoreCase("itunes:image"))
                    {
                        images.add(xpp.nextText());
                    }*/
                    if (insideItem == false) {

                        if (xpp.getName().equalsIgnoreCase("title")) {
                            titles.add(xpp.nextText());
                        }
                        if (xpp.getName().equalsIgnoreCase("itunes:image"))
                        {
                            images.add(xpp.getAttributeValue(null, "href"));
                        }
                    }
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;
                    } else if (xpp.getName().equalsIgnoreCase("itunes:image")) {
                        if (insideItem)
                            ownImages.add(xpp.getAttributeValue(null, "href")); //extract the headline
                    } else if (xpp.getName().equalsIgnoreCase("guid")) {
                        if (insideItem)
                            links.add(xpp.nextText()); //extract the link of article
                    } else if (xpp.getName().equalsIgnoreCase("itunes:summary")) {
                        if (insideItem)
                            //descripcion=removeHtmlTags(xpp.nextText());
                            descriptions.add(Html.fromHtml(xpp.nextText()).toString()); //extract the description of the article
                    } else if (xpp.getName().equalsIgnoreCase("itunes:duration")) {
                        if (insideItem)
                            durations.add(xpp.nextText()); //extract the link of article
                    }

                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                    insideItem = false;
                }

                eventType = xpp.next(); //move to next element
                cont++;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        outer.add(links);

        outer.add(descriptions);

        outer.add(ownImages);

        outer.add(durations);

        outer.add(titles);

        outer.add(images);

        return outer;
    }



    @Override
    protected void onPostExecute(ArrayList<ArrayList<String>> outer) {
        //Recoger todos los arrays una vez finalizado tudo el proceso
        super.onPostExecute(outer);
        if (dialog.isShowing()) {
            dialog.dismiss();
            delegate.processFinish(outer);
        }
    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public String removeHtmlTags(String inStr) {
        int index = 0;
        int index2 = 0;
        while (index != -1) {
            index = inStr.indexOf("<");
            index2 = inStr.indexOf(">", index);
            if (index != -1 && index2 != -1) {
                inStr = inStr.substring(0, index).concat(inStr.substring(index2 + 1, inStr.length()));
            }
        }
        return inStr;
    }

}
