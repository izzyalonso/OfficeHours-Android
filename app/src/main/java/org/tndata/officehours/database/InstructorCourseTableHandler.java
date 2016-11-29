package org.tndata.officehours.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import org.tndata.officehours.database.DatabaseContract.InstructorCourseEntry;
import org.tndata.officehours.model.InstructorCourse;

import java.util.ArrayList;
import java.util.List;


/**
 * Table handler for instructor courses.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class InstructorCourseTableHandler extends TableHandler{

    /*---------*
     * QUERIES *
     *---------*/

    static final String CREATE = "CREATE TABLE " + InstructorCourseEntry.TABLE + " ("
            + InstructorCourseEntry.ID + " INTEGER PRIMARY KEY, "
            + InstructorCourseEntry.CLOUD_ID + " INTEGER, "
            + InstructorCourseEntry.NAME + " TEXT, "
            + InstructorCourseEntry.TIME + " TEXT, "
            + InstructorCourseEntry.CODE + " TEXT)";

    private static final String INSERT = "INSERT INTO " + InstructorCourseEntry.TABLE + " ("
            + InstructorCourseEntry.CLOUD_ID + ", "
            + InstructorCourseEntry.NAME + ", "
            + InstructorCourseEntry.TIME + ", "
            + InstructorCourseEntry.CODE + ") "
            + "VALUES (?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE " + InstructorCourseEntry.TABLE + " SET "
            + InstructorCourseEntry.NAME + "=?, "
            + InstructorCourseEntry.TIME + "=?, "
            + InstructorCourseEntry.CODE + "=? "
            + "WHERE " + InstructorCourseEntry.CLOUD_ID + "=?";

    private static final String SELECT = "SELECT * FROM " + InstructorCourseEntry.TABLE;


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     */
    public InstructorCourseTableHandler(@NonNull Context context){
        init(context);
    }


    /*---------*
     * METHODS *
     *---------*/

    /**
     * Saves an InstructorCourse to the database.
     *
     * @param course the course to be saved.
     */
    public void saveCourse(@NonNull InstructorCourse course){
        //Open a connection to the database
        SQLiteDatabase db = getDatabase();

        //Prepare the statement
        SQLiteStatement stmt = db.compileStatement(INSERT);
        stmt.bindLong(1, course.getId());
        stmt.bindString(2, course.getName());
        stmt.bindString(3, course.getTime());
        stmt.bindString(4, course.getCode());

        //Execute the query
        stmt.executeInsert();

        //Close up
        stmt.close();
    }

    /**
     * Bulk-saves a list of courses to the database.
     *
     * @param courses the list of courses to be saved.
     */
    public void saveCourses(@NonNull List<InstructorCourse> courses){
        //Retrieve a database, begin the transaction, and compile the query
        SQLiteDatabase db = getDatabase();
        db.beginTransaction();
        SQLiteStatement stmt = db.compileStatement(INSERT);

        for (InstructorCourse course:courses){
            //Previous bindings (if any) are emptied
            stmt.clearBindings();

            //Bindings
            stmt.bindLong(1, course.getId());
            stmt.bindString(2, course.getName());
            stmt.bindString(3, course.getTime());
            stmt.bindString(4, course.getCode());

            //Execution
            stmt.executeInsert();
        }

        //Close the transaction
        db.setTransactionSuccessful();
        db.endTransaction();

        //Close the database
        stmt.close();
    }

    /**
     * Updates a course in the database.
     *
     * @param course the course to be updated.
     */
    public void updateCourse(@NonNull InstructorCourse course){
        //Open a connection to the database
        SQLiteDatabase db = getDatabase();

        //Prepare the statement
        SQLiteStatement stmt = db.compileStatement(UPDATE);
        stmt.bindString(1, course.getName());
        stmt.bindString(2, course.getTime());
        stmt.bindString(3, course.getCode());
        stmt.bindLong(4, course.getId());

        //Execute the query
        stmt.execute();

        //Close up
        stmt.close();
    }

    /**
     * Fetches the courses stored in the database.
     *
     * @return the list of courses currently stored in the database.
     */
    public ArrayList<InstructorCourse> getCourses(){
        //Open a readable database and execute the query
        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        ArrayList<InstructorCourse> courses = new ArrayList<>();

        //If there are rows in the cursor returned by the query
        if (cursor.moveToFirst()){
            //For each item
            do{
                //Create the Instructor course and add it to the list
                courses.add(new InstructorCourse(
                        getInt(cursor, InstructorCourseEntry.CLOUD_ID),
                        getString(cursor, InstructorCourseEntry.NAME),
                        getString(cursor, InstructorCourseEntry.TIME),
                        getString(cursor, InstructorCourseEntry.CODE)
                ));
            }
            //Move on until the cursor is empty
            while (cursor.moveToNext());
        }

        //Close both, the cursor and the database
        cursor.close();

        return courses;
    }
}
