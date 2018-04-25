package com.example.tymek.coffeina;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class DrinkCategoryActivity extends ListActivity   {
    private SQLiteDatabase db;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = getListView();

        try{
            SQLiteOpenHelper coffeinaDatabaseHelper = new CoffeinaDatabaseHelper(this);

            db = coffeinaDatabaseHelper.getReadableDatabase();
            cursor = db.query("DRINK",
                                new String[]{"_id", "NAME"},
                                null, null, null,null,null);

            CursorAdapter listAdapter = new SimpleCursorAdapter(this,
                                            android.R.layout.simple_expandable_list_item_1,
                                            cursor,
                                            new String[]{"NAME"},
                                            new int[]{android.R.id.text1}, 0);
            listView.setAdapter(listAdapter);
            Log.d(TAG, "onCreate: koniec");
        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Baza danych jest niedostępna",
                                        Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(DrinkCategoryActivity.this, DrinkActivity.class);
        intent.putExtra("id", (int) id);
        startActivity(intent);
    }
}
