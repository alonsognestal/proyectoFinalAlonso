package com.example.proyectofinalalonso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.proyectofinalalonso.R.id.txtGenero;

/**
 * Created by Alonso on 27/08/2017.
 */

public class AdaptadorNews extends FirebaseRecyclerAdapter<Noticia, AdaptadorNews.EventoViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private View.OnClickListener onClickListener;

    //Al adaptador, en vez de pasarle un vector o un arraylist le tengo que pasar la base de datos de Firebase.
    public AdaptadorNews(int modelLayout, Context context, DatabaseReference ref) {
        super(Noticia.class, modelLayout, AdaptadorNews.EventoViewHolder.class,ref);
        this.context=context;
    }

    public AdaptadorNews(int modelLayout, Context context, Query query) {
        super(Noticia.class, modelLayout, AdaptadorNews.EventoViewHolder.class,query);
        this.context=context;
    }

    //Creamos el viewHolder con las vistas de un elemento
    @Override
    public AdaptadorNews.EventoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(viewGroup.getContext()).inflate(mModelLayout, viewGroup, false);
        view.setOnClickListener(onClickListener);
        return new EventoViewHolder(view);
    }

    @Override
    protected void populateViewHolder(EventoViewHolder holder, Noticia noticia, int position) {
        String txtNoticias = noticia.getNombre();
        holder.txtNoticias.setText(txtNoticias);
        Glide.with(context).load(noticia.geturlimagen()).into(holder.imgNoticias);
    }


    public class EventoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtNoticias)
        TextView txtNoticias;
        @BindView(R.id.imgNoticias)
        ImageView imgNoticias;

        public EventoViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ButterKnife.bind(this, view);
        }

        @Override
    public void onClick(View v) {
        //Al pinchar en una emisora, me lleva a una actividad donde me muestra todos sus datos y un reproductor de música donde se reproduce la emisión online
        int position = getAdapterPosition();
        Noticia noticia = getItem(position);

        Bundle extras = new Bundle();

        extras.putString("rss", noticia.getRss());
        Fragment fragment = new DetalleRssActivity();
        fragment.setArguments(extras);

        FragmentManager fm = ((AppCompatActivity)this.itemView.getContext()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}


}
