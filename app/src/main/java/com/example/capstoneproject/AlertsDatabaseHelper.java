/**
 * Created by Jimmy.
 * */

package com.example.capstoneproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AlertsDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Alerts.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_alerts";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_SYMBOL = "symbol";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CURRENT_PRICE = "currentPrice";
    private static final String COLUMN_ALERT_PRICE = "alertPrice";

    public AlertsDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SYMBOL + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CURRENT_PRICE + " DOUBLE, " +
                COLUMN_ALERT_PRICE + " DOUBLE);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addAlert(String symbol, String name, double currentPrice, double alertPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SYMBOL, symbol);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_CURRENT_PRICE, currentPrice);
        cv.put(COLUMN_ALERT_PRICE, alertPrice);
        long result = db.insert(TABLE_NAME, null, cv);
        //if fail to insert data
        if(result == -1){
            Toast.makeText(context, "Failed to add alert!", Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllDataSorted(String col, String order){
        String query = "SELECT * FROM " + TABLE_NAME+ " Order By "+ col + " "+ order;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        //will contain all data from database
        return cursor;
    }


    //updates current prices in database
    public void updatePriceDatabase(String row_id, String currentPrice){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CURRENT_PRICE, currentPrice);


        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
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




    //get count of rows of alerts database
    public long getAlertsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count;
    }
}
