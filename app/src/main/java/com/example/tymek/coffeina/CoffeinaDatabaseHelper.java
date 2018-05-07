package com.example.tymek.coffeina;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tymek on 2018-04-23.
 */

public class CoffeinaDatabaseHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "coffeina";
    private static final int DB_VERSION = 2;

    public CoffeinaDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion < 1){
            db.execSQL("CREATE TABLE DRINK (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT," +
                    "DESCRIPTION TEXT," +
                    "IMAGE_RESOURCE_ID INTEGER);");
            insertDrink(db, "Latte", "Czarne espresso z gorącym mlekiem i mleczną pianką",
                    R.drawable.latte);
            insertDrink(db, "Cappuccino", "Czarne espresso z dużą ilością spienionego mleka",
                    R.drawable.cappuccino);
            insertDrink(db, "Espresso", "Czarna kawa ze świeżo mielonych ziaren najwyższej jakości",
                    R.drawable.filter);
        }

        if(oldVersion < 2){
            db.execSQL("ALTER TABLE DRINK ADD COLUMN FAVORITE NUMERIC;");
        }
    }

    private static void insertDrink(SQLiteDatabase db, String name, String description, int resourceId){
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("NAME", name);
        drinkValues.put("DESCRIPTION", description);
        drinkValues.put("IMAGE_RESOURCE_ID", resourceId);
        db.insert("DRINK", null, drinkValues);
    }
}
