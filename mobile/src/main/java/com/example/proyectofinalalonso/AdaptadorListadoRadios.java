package com.example.proyectofinalalonso;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.proyectofinalalonso.R.id.txtGenero;

/**
 * Created by Alonso on 02/09/2017.
 */

public class AdaptadorListadoRadios extends FirebaseRecyclerAdapter<EmisoraRadio,  AdaptadorListadoRadios.EventoViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private View.OnClickListener onClickListener;

    //Al adaptador, en vez de pasarle un vector o un arraylist le tengo que pasar la base de datos de Firebase.
    public AdaptadorListadoRadios(int modelLayout,Context context, DatabaseReference ref) {
        super(EmisoraRadio.class, modelLayout, AdaptadorListadoRadios.EventoViewHolder.class,ref);
        this.context = context;
    }

    //Me creo otro constructor pasándole una query en vez de una databaseReference, así le llega solo el filtro que yo quiero (databaseReference.orderByChild("Categoria").equalTo(textoGenero))
    public AdaptadorListadoRadios(int modelLayout,Context context, Query query) {
        super(EmisoraRadio.class, modelLayout, AdaptadorListadoRadios.EventoViewHolder.class,query);
        this.context = context;
    }


    //Creamos el viewHolder con las vistas de un elemento
    @Override
    public EventoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(viewGroup.getContext()).inflate(mModelLayout, viewGroup, false);
        view.setOnClickListener(onClickListener);
        return new EventoViewHolder(view);
    }

    @Override
    protected void populateViewHolder(EventoViewHolder holder, EmisoraRadio emisoraRadio, int position) {

        String txtCategoria = emisoraRadio.getCategoria();
        holder.txtCategoria.setText(txtCategoria);
        String txtRadio = emisoraRadio.getId();
        holder.txtRadio.setText(txtRadio);

        //Me descargo la imagen
        //new DownloadImageTask((ImageView) holder.imgRadio).execute(emisoraRadio.getUrlImagen());
        //Laa librerías Picasso y Glide son muy buenas para cargar las imágenes desde urls, aunque Glide es más rápida.
        //Picasso.with(context).load(emisoraRadio.getUrlImagen()).resize(240,120).into(holder.imgRadio);
        Glide.with(context).load(emisoraRadio.getUrlImagen()).into(holder.imgRadio);
    }


    public class EventoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtCategoria)
        TextView txtCategoria;
        @BindView(R.id.txtRadio)
        TextView txtRadio;

        @BindView(R.id.imgRadio)
        ImageView imgRadio;

        public EventoViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View v) {
            //Al pinchar en una emisora, me lleva a una actividad donde me muestra todos sus datos y un reproductor de música donde se reproduce la emisión online
            int position = getAdapterPosition();
            EmisoraRadio currentItem = getItem(position);
            Intent intent = new Intent(context, DetallesEmisoraRadioActivity.class);
            Bundle extras = new Bundle();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            extras.putString("idEmisora", currentItem.getId());
            extras.putString("imagen", currentItem.getUrlImagen());
            extras.putString("URLAudio", currentItem.getUrlAudio());
            extras.putString("rss", currentItem.getId());
            extras.putString("genero", currentItem.getCategoria());

            intent.putExtras(extras);
            //intent.putExtra("imagenGenero", currentItem.getUrl());
            context.startActivity(intent);
        }
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
