package com.example.tymek.coffeina;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends Activity {

    public static final String EXTRA_DRINKNO = "drinkNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        // w hf uzyto zmiennej statyczne extra_drinkno
        int drinkNumber = (Integer) getIntent().getExtras().get(EXTRA_DRINKNO);

        try {
            SQLiteOpenHelper coffeinaDatabaseHelper = new CoffeinaDatabaseHelper(this);
            SQLiteDatabase db = coffeinaDatabaseHelper.getWritableDatabase();
            Cursor cursor = db.query("DRINK",
                    new String[]{"NAME", "DESCRIPTION",
                            "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                    new String[]{Integer.toString(drinkNumber)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);

                TextView name = (TextView) findViewById(R.id.name);
                name.setText(nameText);

                TextView description = (TextView) findViewById(R.id.description);
                description.setText(descriptionText);

                ImageView photo = (ImageView) findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);


                CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);

            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Baza danych jest niedostępna", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onFavoriteClicked(View view) {
        int drinkNo = (Integer) getIntent().getExtras().get(EXTRA_DRINKNO);
//        CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
//
//        ContentValues drinkValues = new ContentValues();
//
//        drinkValues.put("FAVORITE", favorite.isChecked());
//
//        SQLiteOpenHelper coffeinaDatabaseHelper =
//                new CoffeinaDatabaseHelper(DrinkActivity.this);
//        try {
//            SQLiteDatabase db = coffeinaDatabaseHelper.getWritableDatabase();
//            db.update("DRINK", drinkValues,
//                    "_id = ?", new String[]{Integer.toString(drinkNo)});
//            db.close();
//        } catch (SQLiteException e) {
//            e.printStackTrace();
//            Toast toast = Toast.makeText(this, "Baza danych jest niedostępna", Toast.LENGTH_SHORT);
//            toast.show();
//        }
        new UpdateDrinkTask().execute(drinkNo);
    }

    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean> {
        ContentValues drinkValues;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
            drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favorite.isChecked());
        }

        @Override
        protected Boolean doInBackground(Integer... drinks) {
            int drinkNo = drinks[0];
            SQLiteOpenHelper coffeinaDatabaseHelper = new CoffeinaDatabaseHelper(DrinkActivity.this);
            try {
                SQLiteDatabase db = coffeinaDatabaseHelper.getWritableDatabase();
                db.update("DRINK", drinkValues, "_id = ?", new String[]{Integer.toString(drinkNo)});
                db.close();
                return true;
            } catch (SQLException e) {
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (!success) {
                Toast toast = Toast.makeText(DrinkActivity.this, "Baza danych jest niedostępna", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }
}
