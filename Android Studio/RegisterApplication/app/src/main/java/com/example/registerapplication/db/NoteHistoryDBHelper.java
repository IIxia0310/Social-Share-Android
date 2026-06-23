package com.example.registerapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteHistoryDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sharinglifeapp.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "note_history";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_NOTE_ID = "note_id";
    public static final String COLUMN_BROWSE_TIME = "browse_time";

    // SQL 语句创建表
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " INTEGER NOT NULL, " +
                    COLUMN_NOTE_ID + " INTEGER NOT NULL, " +
                    COLUMN_BROWSE_TIME + " INTEGER NOT NULL);";

    public NoteHistoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}