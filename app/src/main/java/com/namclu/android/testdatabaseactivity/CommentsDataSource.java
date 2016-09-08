package com.namclu.android.testdatabaseactivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by namlu on 07-Sep-16.
 *
 * CommentsDataSource.java is our Data Access Object (DAO).
 * It maintains the database connection and supports adding new comments and fetching all comments.
 */
public class CommentsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    // allColumn is an array consisting of "_id", "comment"
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_COMMENT };

    // Context (android.content)
    public CommentsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public  void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // ContentValues (android.content)
    public Comment createComment(String comment) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        // .insert(String table, String nullColumnHack, ContentValues values)
        long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null, values);
        // Cursor (android.database) - interface provides random read-write access
        // to the result set returned by a database query
        // .query() queries the given table, returning a Cursor over the result set
        // .query(String table, String[] columns, String selection, String[] selectionArgs,
        //      String groupBy, String having, String orderBy)
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns,
                MySQLiteHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null);
        // boolean .moveToFirst() - move the cursor to the first row
        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        cursor.close();

        return newComment;
    }

    public void deleteComment(Comment comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        // .delete(String table, String whereClause, String[] whereArgs)
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Comment> getAllComments() {

        List<Comment> comments = new ArrayList<Comment>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS, allColumns,
                null, null, null, null, null);

        // boolean .moveToFirst() - move the cursor to the first row
        cursor.moveToFirst();
        // boolean .isAfterLast() - returns if the cursor is pointing to the position after the last row
        while (!cursor.isAfterLast()) {
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        cursor.close();

        return comments;
    }

    private Comment cursorToComment(Cursor cursor) {
        Comment comment = new Comment();
        comment.setId(cursor.getLong(0));
        comment.setComment(cursor.getString(1));

        return comment;
    }
}
