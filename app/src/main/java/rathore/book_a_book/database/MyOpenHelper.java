package rathore.book_a_book.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import rathore.book_a_book.tables.UserDataTable;

/**
 * Created by Rathore on 24-Jun-17.
 */

public class MyOpenHelper extends SQLiteOpenHelper{
    private Context context;
    
    public MyOpenHelper(Context context){
        super(context,"MyDB.db",null,2);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Toast.makeText(context, "Database created", Toast.LENGTH_SHORT).show();
        UserDataTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context, "Database Upgraded", Toast.LENGTH_SHORT).show();
        UserDataTable.upgradeTable(db);
    }
}
