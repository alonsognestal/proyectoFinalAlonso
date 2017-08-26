package com.example.proyectofinalalonso;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Alonso on 20/08/2017.
 */

public class Genero {
    public String titulo;
    public String url;

    public final static String G_TODOS = "Todos los géneros";
    public final static String G_EPICO = "Poema épico";
    public final static String G_S_XIX = "Literatura siglo XIX";
    public final static String G_SUSPENSE = "Suspense";
    public final static String[] G_ARRAY = new String[] {G_TODOS, G_EPICO,
            G_S_XIX, G_SUSPENSE };

    public Genero()
    {

    }
    public Genero(String titulo, String url) {
        this.titulo = titulo;
        this.url = url;

    }
    public static ArrayList<Genero> ejemploGeneros() {
        final String SERVIDOR =
                "http://www.dcomg.upv.es/~jtomas/android/audioGeneros/";
        ArrayList<Genero> Generos = new ArrayList<Genero>();
        /*Generos.add(new Genero("Kappa", "Akutagawa",
                R.drawable.kappa, SERVIDOR+"kappa.mp3",
                Genero.G_S_XIX, false, false));
        Generos.add(new Genero("Avecilla", "Alas Clarín, Leopoldo",
                R.drawable.avecilla, SERVIDOR+"avecilla.mp3",
                Genero.G_S_XIX, true, false));
       */
        return Generos;
    }

    public String getTitulo()
    {
        return titulo;
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}
