package com.example.proyectofinalalonso;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;

/**
 * Created by Alonso on 02/09/2017.
 */

public class AdaptadorPodcastsPorRadio extends RecyclerView.Adapter<AdaptadorPodcastsPorRadio.ItemViewHolder> {
    private LayoutInflater inflador;
    private static Context contexto;
    //ArrayList<ArrayList<String>> outer = new ArrayList<ArrayList<String>>();
    //ArrayList<String> inner = new ArrayList<String>();
    private static RecyclerViewClickListener itemListener;
    Object podcast = new Object();


    public AdaptadorPodcastsPorRadio(Context contexto, Object podcast, RecyclerViewClickListener itemListener) {
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.podcast = podcast;
        this.contexto = contexto;
        this.itemListener = itemListener;

    } //Creamos nuestro ViewHolder, con los tipos de elementos a modificar



    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // Inflamos la vista desde el xml
        View v = inflador.inflate(R.layout.content_podcasts_por_radio, null);

        return new ItemViewHolder(v);
    } // Usando como base el ViewHolder y lo personalizamos

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        /* ArrayList<String> links = outer.get(posicion);*/
        if (((HashMap) podcast).size() > 0) {

            holder.idPodcast.setText(((HashMap) podcast).get("idPodcast").toString());
            //holder.rss.setText(((HashMap)podcast).get("rss").toString());
            Glide.with(contexto).load(((HashMap) podcast).get("imagen")).into(holder.imagen);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imagen;
        public TextView rss;
        public TextView idPodcast;

        public ItemViewHolder(View convertView)
        {
            super(convertView);

            //imagen = (ImageView) itemView.findViewById(R.id.imagen);
            //imagen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            rss = (TextView) itemView.findViewById(R.id.rss);
            idPodcast = (TextView) itemView.findViewById(R.id.idPodcast);
            imagen = (ImageView) itemView.findViewById(R.id.imagen);
            convertView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v)
        {
            try {
                itemListener.recyclerViewListClicked(v, this.getPosition());
                String imagen = ((HashMap) ((ListadoPodcastsPorRadio) itemListener).podcast1).get("imagen").toString();
                String rss = ((HashMap) ((ListadoPodcastsPorRadio) itemListener).podcast1).get("rss").toString();
                String idPodcast = ((HashMap) ((ListadoPodcastsPorRadio) itemListener).podcast1).get("idPodcast").toString();

                Context context = Aplicacion.getAppContext();
                Intent intent = new Intent(context, PodcastsPorPrograma.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("imagen", imagen);
                intent.putExtra("rss", rss);
                intent.putExtra("idPodcast", idPodcast);
                context.startActivity(intent);
            }
            catch (Exception ex)
            {

            }
        }

    }
    @Override
    public int getItemCount() {
        return 1;
    }
}

