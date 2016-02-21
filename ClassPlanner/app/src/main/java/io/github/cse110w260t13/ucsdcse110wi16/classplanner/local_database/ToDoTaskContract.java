package io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database;

/**
 * Created by JamesOh on 1/30/2016.
 * DEFINING CONSTANT VARIABLES TO ACCESS DATABASE
 */
import android.provider.BaseColumns;

public class ToDoTaskContract {
    public static final String DB_NAME= "toDoList.db";
    public static final int DB_VERSION= 1;
    public static final String TABLE  = "tasks";

            public class Columns {
                public static final String TASK = "task";
                public static final String _ID = BaseColumns._ID;
            }

}
