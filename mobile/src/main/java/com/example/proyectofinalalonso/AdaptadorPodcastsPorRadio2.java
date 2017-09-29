package com.example.proyectofinalalonso;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alonso on 02/09/2017.
 */

public class AdaptadorPodcastsPorRadio2 extends RecyclerView.Adapter<AdaptadorPodcastsPorRadio2.ViewHolder> {
    private LayoutInflater inflador;
    private static Context contexto;
    //ArrayList<ArrayList<String>> outer = new ArrayList<ArrayList<String>>();
    //ArrayList<String> inner = new ArrayList<String>();
    private View.OnClickListener onClickListener;
    Object podcast = new Object();


    public AdaptadorPodcastsPorRadio2(Context contexto, Object podcast) {
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.podcast = podcast;
        this.contexto = contexto;
    } //Creamos nuestro ViewHolder, con los tipos de elementos a modificar

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagen;
        public TextView rss;
        public TextView idPodcast;

        public ViewHolder(View itemView) {
            super(itemView);
            //imagen = (ImageView) itemView.findViewById(R.id.imagen);
            //imagen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            rss = (TextView) itemView.findViewById(R.id.rss);
            idPodcast = (TextView) itemView.findViewById(R.id.idPodcast);
            imagen = (ImageView)itemView.findViewById(R.id.imagen);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // Inflamos la vista desde el xml
        View v = inflador.inflate(R.layout.elemento_podcast, null);
        return new ViewHolder(v);
    } // Usando como base el ViewHolder y lo personalizamos

    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
       /* ArrayList<String> links = outer.get(posicion);*/
        if (((HashMap)podcast).size()>0)
        {

            holder.idPodcast.setText(((HashMap)podcast).get("idPodcast").toString());
            //holder.rss.setText(((HashMap)podcast).get("rss").toString());
            Glide.with(contexto).load(((HashMap)podcast).get("imagen")).into(holder.imagen);
        }

       /* Libro libro = vectorLibros.elementAt(posicion);
        holder.portada.setImageResource(libro.recursoImagen);
        holder.titulo.setText(libro.titulo);*/
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}

