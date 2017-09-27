package com.example.proyectofinalalonso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.id;

/**
 * Created by Alonso on 26/08/2017.
 */

public class EmisoraRadio {
    private String id;
    private String URLImagen;
    private String URLAudio;
    private String Categoria;
    private String rss;
    private HashMap<String, Object> Podcast = new HashMap<String, Object>();

    public EmisoraRadio()
    {

    }

    public EmisoraRadio(String id, String urlImagen, String urlAudio, String categoria, String rss, HashMap<String, Object> podcast )
    {
        this.id = id;
        this.URLImagen = urlImagen;
        this.URLAudio = urlAudio;
        this.Categoria = categoria;
        this.rss = rss;
        this.Podcast = podcast;
    }

    public String getId() {
        return id;
    }

    public String getUrlAudio() {
            return URLAudio;
    }

    public String getUrlImagen() {
        return URLImagen;
    }

    public String getCategoria()
    {
        return Categoria;
    }

    public String getRSS()
    {
        return rss;
    }

    public HashMap<String, Object> getPodcast() { return Podcast;}

    public void setUrlAudio(String urlAudio)
    {
        this.URLAudio = urlAudio;
    }

    public void setUrlImagen(String urlImagen)
    {
        this.URLImagen = urlImagen;
    }

    public void setCategoria(String categoria)
    {
        this.Categoria = categoria;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setRSS(String rss)
    {
        this.rss = rss;
    }

    public void setPodcast(HashMap<String, Object> podcast)
    {
        this.Podcast = podcast;
    }
}
