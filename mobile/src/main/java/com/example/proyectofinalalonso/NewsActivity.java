package com.example.proyectofinalalonso;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Alonso on 20/08/2017.
 */

public class NewsActivity extends Activity
{
    private final static String BOOKS_CHILD = "libros";
    private final static String USERS_CHILD = "usuarios";
    private DatabaseReference usersReference;


    public void onCreate() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        DatabaseReference booksReference =
                database.getReference().child(BOOKS_CHILD);
        usersReference = database.getReference().child(USERS_CHILD);
    }
}
