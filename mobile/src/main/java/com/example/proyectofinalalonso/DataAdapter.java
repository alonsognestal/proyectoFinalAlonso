package com.example.proyectofinalalonso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataAdapter extends FirebaseRecyclerAdapter<Genero, DataAdapter.EventoViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<Genero> listaGeneros;
    private Context context;

    //Al adaptador, en vez de pasarle un vector o un arraylist le tengo que pasar la base de datos de Firebase.
    public DataAdapter(int modelLayout, DatabaseReference ref) {
        super(Genero.class, modelLayout, DataAdapter.EventoViewHolder.class,ref);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listaGeneros = listaGeneros;
        this.context = context;
    }

    //Creamos el viewHolder con las vistas de un elemento
    @Override
    public DataAdapter.EventoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.content_radio, viewGroup, false);
        return new EventoViewHolder(view);
    }

    //Usamos como base el ViewHolder y lo personalizamos
    /*@Override
    public void onBindViewHolder(DataAdapter.EventoViewHolder viewHolder, int i) {

        viewHolder.txtGenero.setText(listaGeneros.get(i).getTitulo());
        Picasso.with(context).load(listaGeneros.get(i).getUrl()).resize(240, 120).into(viewHolder.imgGenero);
    }*/

    @Override
    protected void populateViewHolder(EventoViewHolder holder, Genero genero, int position) {
        String txtGenero = genero.getTitulo();

        holder.txtGenero.setText(txtGenero);
        new DownloadImageTask((ImageView) holder.imgGenero).execute(genero.getTitulo());
    }

    @Override
    public int getItemCount() {
        return listaGeneros.size();
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

}