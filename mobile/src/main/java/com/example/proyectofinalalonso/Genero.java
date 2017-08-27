package com.example.proyectofinalalonso;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Alonso on 20/08/2017.
 */

public class Genero {
    public String nombre;
    public String urlImagen;


    public Genero()
    {

    }
    public Genero(String nombre, String urlImagen) {
        this.nombre = nombre;
        this.urlImagen = urlImagen;

    }

    public String getTitulo()
    {
        return nombre;
    }

    public void setTitulo(String nombre)
    {
        this.nombre = nombre;
    }

    public String getUrl()
    {
        return urlImagen;
    }

    public void setUrl(String url)
    {
        this.urlImagen = url;
    }
}
