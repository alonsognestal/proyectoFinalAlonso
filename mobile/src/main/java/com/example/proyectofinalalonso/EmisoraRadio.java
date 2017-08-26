package com.example.proyectofinalalonso;

/**
 * Created by Alonso on 26/08/2017.
 */

public class EmisoraRadio {
    private String urlImagen;
    private String urlAudio;
    private String categoria;

    public EmisoraRadio(String urlImagen, String urlAudio, String categoria)
    {
        this.urlImagen = urlImagen;
        this.urlAudio = urlAudio;
        this.categoria = categoria;
    }

    public String getUrlAudio() {
            return urlAudio;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public String getCategoria()
    {
        return categoria;
    }

    public void setUrlAudio(String urlAudio)
    {
        this.urlAudio = urlAudio;
    }

    public void setUrlImagen(String urlImagen)
    {
        this.urlImagen = urlImagen;
    }

    public void setCategoria(String categoria)
    {
        this.categoria = categoria;
    }
}
