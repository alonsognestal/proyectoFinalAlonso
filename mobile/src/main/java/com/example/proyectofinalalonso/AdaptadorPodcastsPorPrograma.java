package com.example.proyectofinalalonso;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.proyectofinalalonso.R.id.idPodcast;
import static com.example.proyectofinalalonso.R.id.imagen;
import static com.example.proyectofinalalonso.R.id.rss;

/**
 * Created by Alonso on 02/09/2017.
 */

public class AdaptadorPodcastsPorPrograma extends RecyclerView.Adapter<AdaptadorPodcastsPorPrograma.ItemViewHolder> {
    private LayoutInflater inflador;
    private Context contexto;
    ArrayList<ArrayList<String>> outer = new ArrayList<ArrayList<String>>();
    ArrayList<String> inner = new ArrayList<String>();
    private static RecyclerViewClickListener itemListener;


    public AdaptadorPodcastsPorPrograma(Context contexto, ArrayList<ArrayList<String>> outer, RecyclerViewClickListener itemListener) {
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.outer = outer;
        this.inner = inner;
        this.contexto = contexto;
        this.itemListener = itemListener;
    } //Creamos nuestro ViewHolder, con los tipos de elementos a modificar


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // Inflamos la vista desde el xml
        View v = inflador.inflate(R.layout.elemento_podcast, null);
        return new ItemViewHolder(v);
    } // Usando como base el ViewHolder y lo personalizamos


    @Override
    public void onBindViewHolder(ItemViewHolder holder, int posicion) {
        ArrayList<String> links = outer.get(posicion);
        if (links.size()>0)
        {
            holder.url.setText(links.get(0));
            holder.descripcion.setText(links.get(1));
            Glide.with(contexto).load(links.get(2)).into(holder.imagen);
        }
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imagen;
        public TextView descripcion;
        public TextView url;

        public ItemViewHolder(View convertView)
        {
            super(convertView);
            imagen = (ImageView) itemView.findViewById(R.id.imagen);
            imagen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            descripcion = (TextView) itemView.findViewById(idPodcast);
            url = (TextView) itemView.findViewById(rss);
            convertView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v)
        {
            //TODO: INCOMPLETO
            itemListener.recyclerViewListClicked(v, this.getPosition());
            (( ArrayList<ArrayList<String>>)((PodcastsPorPrograma)itemListener).listado).get(0);
           /* Bundle bundle = new Bundle();
            String imagen = ((HashMap) ((PodcastsPorPrograma) itemListener).listado).get("imagen").toString();
            String rss = ((HashMap) ((PodcastsPorPrograma) itemListener).podcast1).get("rss").toString();
            String idPodcast = ((HashMap) ((PodcastsPorPrograma) itemListener).podcast1).get("idPodcast").toString();
*/
            Context context = Aplicacion.getAppContext();
            //(( ArrayList<ArrayList<String>>)((PodcastsPorPrograma)itemListener).listado);
            /*bundle.putString("imagen", imagen);
            bundle.putString("rss", rss);
            bundle.putString("idPodcast", idPodcast);*/
            Intent intent = new Intent(context, DetallePodcastActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            /*intent.putExtra("imagen", imagen);
            intent.putExtra("rss", rss);
            intent.putExtra("idPodcast", idPodcast);*/
            context.startActivity(intent);
        }

    }
    @Override
    public int getItemCount() {
        return 1;
    }
}
