package org.tndata.officehours.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

/**
 * The application's database helper. A skeleton so far.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "office_hours.db";

    //Initial version, TBD
    private static final int V1 = 1;

    //Current version, V1
    private static final int CURRENT_VERSION = V1;


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     */
    DatabaseHelper(@NonNull Context context){
        super(context, DATABASE_NAME, null, CURRENT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(InstructorCourseTableHandler.CREATE);
        db.execSQL(StudentCourseTableHandler.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
