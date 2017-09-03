package com.example.proyectofinalalonso;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdaptadorRadio extends FirebaseRecyclerAdapter<Genero, AdaptadorRadio.EventoViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private View.OnClickListener onClickListener;

    //Al adaptador, en vez de pasarle un vector o un arraylist le tengo que pasar la base de datos de Firebase.
    public AdaptadorRadio(int modelLayout, Context context, DatabaseReference ref) {
        super(Genero.class, modelLayout, AdaptadorRadio.EventoViewHolder.class, ref);
        this.context = context;
    }

    //Creamos el viewHolder con las vistas de un elemento
    @Override
    public AdaptadorRadio.EventoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(viewGroup.getContext()).inflate(mModelLayout, viewGroup, false);
        view.setOnClickListener(onClickListener);
        return new EventoViewHolder(view);
    }

    @Override
    protected void populateViewHolder(EventoViewHolder holder, Genero genero, int position) {
        String txtGenero = genero.getTitulo();
        holder.txtGenero.setText(txtGenero);
        //new DownloadImageTask((ImageView) holder.imgGenero).execute(genero.getUrl());
        Glide.with(context).load(genero.getUrl()).into(holder.imgGenero);
    }


    public class EventoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtGenero)
        TextView txtGenero;
        @BindView(R.id.imgGenero)
        ImageView imgGenero;

        public EventoViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Genero currentItem = (Genero) getItem(position);
            Context context = Aplicacion.getAppContext();
            Intent intent = new Intent(context, ListadoRadiosActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("textoGenero", currentItem.nombre);
            //intent.putExtra("imagenGenero", currentItem.getUrl());
            context.startActivity(intent);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mImagen = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mImagen = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mImagen;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}