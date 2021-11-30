/**
 * Created by Jimmy.
 * */


package com.example.capstoneproject.fragments.alerts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SortDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "SortAlerts.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "sort_alerts";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE_ASC = "dateAsc";
    private static final String COLUMN_DATE_DESC = "dateDesc";
    private static final String COLUMN_NAME_ASC = "nameAsc";
    private static final String COLUMN_NAME_DESC = "nameDesc";
    private static final String COLUMN_PRICE_ASC = "priceAsc";
    private static final String COLUMN_PRICE_DESC = "priceDesc";

    public SortDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE_ASC + " INTEGER, " +
                COLUMN_DATE_DESC + " INTEGER, " +
                COLUMN_NAME_ASC + " INTEGER, " +
                COLUMN_NAME_DESC + " INTEGER, " +
                COLUMN_PRICE_ASC + " INTEGER, " +
                COLUMN_PRICE_DESC + " INTEGER);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addSort(int dateAsc, int dateDesc, int nameAsc, int nameDesc, int priceAsc, int priceDesc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DATE_ASC, dateAsc);
        cv.put(COLUMN_DATE_DESC, dateDesc);
        cv.put(COLUMN_NAME_ASC, nameAsc);
        cv.put(COLUMN_NAME_DESC, nameDesc);
        cv.put(COLUMN_PRICE_ASC, priceAsc);
        cv.put(COLUMN_PRICE_DESC, priceDesc);

        long result = db.insert(TABLE_NAME, null, cv);
        //if fail to insert data
        if(result == -1){
            Toast.makeText(context, "Failed to set sorting setting", Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readSortSetting(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        //will contain all data from database
        return cursor;
    }

    //updates current prices in database
    public void updateSortSetting(String dateAsc, String dateDesc, String nameAsc, String nameDesc, String priceAsc, String priceDesc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE_ASC, dateAsc);
        cv.put(COLUMN_DATE_DESC, dateDesc);
        cv.put(COLUMN_NAME_ASC, nameAsc);
        cv.put(COLUMN_NAME_DESC, nameDesc);
        cv.put(COLUMN_PRICE_ASC, priceAsc);
        cv.put(COLUMN_PRICE_DESC, priceDesc);


        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{"1"});
        if(result == -1){
            //Toast.makeText(context, "Failed to update prices!", Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    //delete row in database
    public void deleteRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete!", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    //get count of rows of sort database
    public long getSortCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count;
    }

}
