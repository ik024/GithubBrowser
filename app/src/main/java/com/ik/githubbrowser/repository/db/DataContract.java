package com.ik.githubbrowser.repository.db;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DataContract {
    public static final String DB_NAME = "git.db";
    public static final int DB_VERSION = 1;

    public static final String SCHEME = "content://";
    public static final String AUTHORITY= "com.ik.githubbrowser";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    public static class LastVisitedUser implements BaseColumns {
        public static final String TABLE_NAME = "last_visited";

        public static final String COL_USERNAME = "username";


        public static final String CREATE_TABLE_FAVOURITE_USERNAME = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID  + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USERNAME + " VARCHAR NOT NULL" +
                ")";

        public static final Uri LIST_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static Uri ITEM_URI(long id){
            return ContentUris.withAppendedId(
                    BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build(),
                    id);
        }
        public static final String TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                AUTHORITY + "/" + TABLE_NAME;

        public static final String TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                AUTHORITY + "/" + TABLE_NAME;
    }
}
