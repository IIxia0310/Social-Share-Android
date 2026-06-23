package com.example.registerapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.registerapplication.Entity.Data.NoteHistoryltem;

import java.util.ArrayList;
import java.util.List;

public class NoteHistoryDao {
    private NoteHistoryDBHelper dbHelper;

    public NoteHistoryDao(Context context) {
        dbHelper = new NoteHistoryDBHelper(context);
    }

    // 添加浏览历史记录
    public long addHistory(NoteHistoryltem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteHistoryDBHelper.COLUMN_USER_ID, item.getUserId());
        values.put(NoteHistoryDBHelper.COLUMN_NOTE_ID, item.getNoteId());
        values.put(NoteHistoryDBHelper.COLUMN_BROWSE_TIME, item.getBrowseTime());

        long rowId = db.insert(NoteHistoryDBHelper.TABLE_NAME, null, values);
        db.close();
        return rowId;
    }

    // 获取用户的浏览历史记录
    public List<NoteHistoryltem> getHistoryByUserId(Long userId) {
        List<NoteHistoryltem> historyList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                NoteHistoryDBHelper.COLUMN_ID,
                NoteHistoryDBHelper.COLUMN_USER_ID,
                NoteHistoryDBHelper.COLUMN_NOTE_ID,
                NoteHistoryDBHelper.COLUMN_BROWSE_TIME
        };

        String selection = NoteHistoryDBHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = { userId.toString() };
        String sortOrder = NoteHistoryDBHelper.COLUMN_BROWSE_TIME + " DESC"; // 按时间倒序排列

        Cursor cursor = db.query(
                NoteHistoryDBHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor.moveToFirst()) {
            do {
                NoteHistoryltem item = new NoteHistoryltem(
                        cursor.getLong(cursor.getColumnIndex(NoteHistoryDBHelper.COLUMN_USER_ID)),
                        cursor.getLong(cursor.getColumnIndex(NoteHistoryDBHelper.COLUMN_NOTE_ID)),
                        cursor.getLong(cursor.getColumnIndex(NoteHistoryDBHelper.COLUMN_BROWSE_TIME))
                );
                item.setId(cursor.getLong(cursor.getColumnIndex(NoteHistoryDBHelper.COLUMN_ID)));
                historyList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return historyList;
    }

    // 检查笔记是否已被浏览过
    public boolean hasBrowsed(Long userId, Long noteId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = { NoteHistoryDBHelper.COLUMN_ID };
        String selection = NoteHistoryDBHelper.COLUMN_USER_ID + " = ? AND " +
                NoteHistoryDBHelper.COLUMN_NOTE_ID + " = ?";
        String[] selectionArgs = { userId.toString(), noteId.toString() };

        Cursor cursor = db.query(
                NoteHistoryDBHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean hasBrowsed = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return hasBrowsed;
    }

    // 更新浏览时间（如果笔记已被浏览过）
    public int updateBrowseTime(Long userId, Long noteId, Long newTime) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteHistoryDBHelper.COLUMN_BROWSE_TIME, newTime);

        String selection = NoteHistoryDBHelper.COLUMN_USER_ID + " = ? AND " +
                NoteHistoryDBHelper.COLUMN_NOTE_ID + " = ?";
        String[] selectionArgs = { userId.toString(), noteId.toString() };

        int count = db.update(
                NoteHistoryDBHelper.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        db.close();
        return count;
    }

    // 删除指定用户的所有浏览历史
    public void deleteAllHistory(Long userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = NoteHistoryDBHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = { userId.toString() };

        db.delete(NoteHistoryDBHelper.TABLE_NAME, selection, selectionArgs);
        db.close();
    }
}