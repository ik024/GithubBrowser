package com.ik.githubbrowser.repository.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper dbHelper;

    public static DBHelper getInstance(Context context){
        if (dbHelper == null)
            dbHelper = new DBHelper(context);

        return dbHelper;
    }

    private DBHelper(Context context) {
        super(context, DataContract.DB_NAME, null, DataContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataContract.LastVisitedUser.CREATE_TABLE_FAVOURITE_USERNAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ DataContract.LastVisitedUser.TABLE_NAME);
        onCreate(db);
    }
}
