package com.example.proyectofinalalonso;

/**
 * Created by Alonso on 14/09/2017.
 */

public class Noticia {
    private String nombre;
    private String urlimagen;
    private String rss;

    public Noticia()
    {

    }
    public Noticia(String nombre, String urlimagen, String rss) {
        this.nombre = nombre;
        this.urlimagen = urlimagen;
        this.rss = rss;

    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String geturlimagen()
    {
        return urlimagen;
    }

    public void seturlimagen(String urlimagen)
    {
        this.urlimagen = urlimagen;
    }

    public String getRss()
    {
        return rss;
    }

    public void setRss(String rss)
    {
        this.rss = rss;
    }
}
