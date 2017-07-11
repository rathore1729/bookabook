package rathore.book_a_book.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Rathore on 05-Jul-17.
 */

public class UserDataTable {
    public static final String TABLE_NAME = "UserDataTable";
    public static final String NAME = "Name";
    public static final String EMAIL = "Email";
    public static final String PASSWORD = "Password";
    public static final String PHONE = "Phone";
    public static final String ADDRESS = "Address";
    public static final String BACKCOLOR = "BackColor";
    public static final String VERIFICATION = "Verification";
    public static final String SEC_QUES = "SecQues";
    public static final String ANSWER = "Answer";
    public static final String QUERY = "CREATE TABLE \"UserDataTable\" ( `Name` TEXT NOT NULL, `Email` TEXT, `Password` INTEGER NOT NULL, `Phone` TEXT NOT NULL, `Address` TEXT, `BackColor` INTEGER NOT NULL, `Verification` TEXT NOT NULL, `SecQues` TEXT NOT NULL, `Answer` TEXT NOT NULL, PRIMARY KEY(`Email`) )";

    public static void createTable(SQLiteDatabase db){
        db.execSQL(QUERY);
        Log.d("011", "createTable:created ");
    }

    public static void upgradeTable(SQLiteDatabase db){
        db.execSQL("drop table if exists " + TABLE_NAME);
        createTable(db);
    }

    public static long insert(SQLiteDatabase db, ContentValues cv){
        return db.insert(TABLE_NAME,null,cv);
    }

    public static Cursor select(SQLiteDatabase db, String selection){
        return db.query(TABLE_NAME,null,selection,null,null,null,null);
    }

    public static Cursor verifyAccount(SQLiteDatabase db, String selection){
        String[] column = {EMAIL,PASSWORD,VERIFICATION};
        return db.query(TABLE_NAME,column,selection,null,null,null,null);
    }

    public static Cursor retrievePassword(SQLiteDatabase db, String selection){
        String[] column = {EMAIL,SEC_QUES,ANSWER};
        return db.query(TABLE_NAME,column,selection,null,null,null,null);
    }

    public static Cursor askEmail(SQLiteDatabase db, String selection){
        String[] column = {EMAIL};
        return db.query(TABLE_NAME,column,selection,null,null,null,null);
    }

    public static int update(SQLiteDatabase db,ContentValues cv, String selection){
        return db.update(TABLE_NAME,cv,selection,null);
    }

    public static int delete(SQLiteDatabase db,String selection){
        return db.delete(TABLE_NAME,selection,null);
    }
}