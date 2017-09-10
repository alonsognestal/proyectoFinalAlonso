package com.example.proyectofinalalonso;

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

    public EmisoraRadio()
    {

    }

    public EmisoraRadio(String id, String urlImagen, String urlAudio, String categoria, String rss)
    {
        this.id = id;
        this.URLImagen = urlImagen;
        this.URLAudio = urlAudio;
        this.Categoria = categoria;
        this.rss = rss;
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
}
