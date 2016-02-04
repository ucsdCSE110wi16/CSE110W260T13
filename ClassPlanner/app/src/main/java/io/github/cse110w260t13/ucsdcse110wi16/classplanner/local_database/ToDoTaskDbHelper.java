package io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database;

/**
 * Created by JamesOh on 1/30/2016.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.ToDoFragment;

public class ToDoTaskDbHelper extends SQLiteOpenHelper{

    public ToDoTaskDbHelper(Context context){
        super(context, ToDoTaskContract.DB_NAME,null,ToDoTaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {

        String sqlQuery =
                String.format("CREATE TABLE %s (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT)",ToDoTaskContract.TABLE,ToDoTaskContract.Columns.TASK);

        Log.d("ToDoTaskDbHelper","Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + ToDoTaskContract.TABLE);
        onCreate(sqlDB);
    }

}
