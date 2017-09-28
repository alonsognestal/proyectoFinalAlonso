package com.example.proyectofinalalonso;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
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
import java.util.Objects;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.proyectofinalalonso.R.id.imagen;

/**
 * Created by Alonso on 02/09/2017.
 */

public class AdaptadorPodcastsPorRadio extends RecyclerView.Adapter<AdaptadorPodcastsPorRadio.ViewHolder> {
    private LayoutInflater inflador;
    private Context contexto;
    ArrayList<ArrayList<String>> outer = new ArrayList<ArrayList<String>>();
    ArrayList<String> inner = new ArrayList<String>();
    private View.OnClickListener onClickListener;


    public AdaptadorPodcastsPorRadio(Context contexto, ArrayList<ArrayList<String>> outer) {
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.outer = outer;
        this.contexto = contexto;
    } //Creamos nuestro ViewHolder, con los tipos de elementos a modificar

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagen;
        public TextView descripcion;
        public TextView url;

        public ViewHolder(View itemView) {
            super(itemView);
            imagen = (ImageView) itemView.findViewById(R.id.imagen);
            imagen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            descripcion = (TextView) itemView.findViewById(R.id.descripcion);
            url = (TextView) itemView.findViewById(R.id.url);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // Inflamos la vista desde el xml
       View v = inflador.inflate(R.layout.elemento_podcast, null);
        return new ViewHolder(v);
    } // Usando como base el ViewHolder y lo personalizamos

    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        ArrayList<String> links = outer.get(posicion);
        if (inner.size()>0)
        {
            holder.url.setText(inner.get(0));
            holder.descripcion.setText(inner.get(1));
            Glide.with(contexto).load(inner.get(2)).into(holder.imagen);
        }

       /* Libro libro = vectorLibros.elementAt(posicion);
        holder.portada.setImageResource(libro.recursoImagen);
        holder.titulo.setText(libro.titulo);*/
    }

    @Override
    public int getItemCount() {
        return outer.size();
    }
}
