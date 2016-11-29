package org.tndata.officehours.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import org.tndata.officehours.database.DatabaseContract.StudentCourseEntry;
import org.tndata.officehours.model.StudentCourse;

import java.util.ArrayList;
import java.util.List;


/**
 * Table handler for student courses.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class StudentCourseTableHandler extends TableHandler{
    
    /*---------*
     * QUERIES *
     *---------*/

    static final String CREATE = "CREATE TABLE " + StudentCourseEntry.TABLE + " ("
            + StudentCourseEntry.ID + " INTEGER PRIMARY KEY, "
            + StudentCourseEntry.CLOUD_ID + " INTEGER, "
            + StudentCourseEntry.NAME + " TEXT, "
            + StudentCourseEntry.TIME + " TEXT, "
            + StudentCourseEntry.INSTRUCTOR + " TEXT)";

    private static final String INSERT = "INSERT INTO " + StudentCourseEntry.TABLE + " ("
            + StudentCourseEntry.CLOUD_ID + ", "
            + StudentCourseEntry.NAME + ", "
            + StudentCourseEntry.TIME + ", "
            + StudentCourseEntry.INSTRUCTOR + ") "
            + "VALUES (?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE " + StudentCourseEntry.TABLE + " SET "
            + StudentCourseEntry.NAME + "=?, "
            + StudentCourseEntry.TIME + "=?, "
            + StudentCourseEntry.INSTRUCTOR + "=? "
            + "WHERE " + StudentCourseEntry.CLOUD_ID + "=?";

    private static final String SELECT = "SELECT * FROM " + StudentCourseEntry.TABLE;


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     */
    public StudentCourseTableHandler(@NonNull Context context){
        init(context);
    }


    /*---------*
     * METHODS *
     *---------*/

    /**
     * Saves a StudentCourse to the database.
     *
     * @param course the course to be saved.
     */
    public void saveCourse(@NonNull StudentCourse course){
        //Open a connection to the database
        SQLiteDatabase db = getDatabase();

        //Prepare the statement
        SQLiteStatement stmt = db.compileStatement(INSERT);
        stmt.bindLong(1, course.getId());
        stmt.bindString(2, course.getName());
        stmt.bindString(3, course.getTime());
        stmt.bindString(4, course.getInstructor());

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
    public void saveCourses(@NonNull List<StudentCourse> courses){
        //Retrieve a database, begin the transaction, and compile the query
        SQLiteDatabase db = getDatabase();
        db.beginTransaction();
        SQLiteStatement stmt = db.compileStatement(INSERT);

        for (StudentCourse course:courses){
            //Previous bindings (if any) are emptied
            stmt.clearBindings();

            //Bindings
            stmt.bindLong(1, course.getId());
            stmt.bindString(2, course.getName());
            stmt.bindString(3, course.getTime());
            stmt.bindString(4, course.getInstructor());

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
    public void updateCourse(@NonNull StudentCourse course){
        //Open a connection to the database
        SQLiteDatabase db = getDatabase();

        //Prepare the statement
        SQLiteStatement stmt = db.compileStatement(UPDATE);
        stmt.bindString(1, course.getName());
        stmt.bindString(2, course.getTime());
        stmt.bindString(3, course.getInstructor());
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
    public ArrayList<StudentCourse> getCourses(){
        //Open a readable database and execute the query
        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        ArrayList<StudentCourse> courses = new ArrayList<>();

        //If there are rows in the cursor returned by the query
        if (cursor.moveToFirst()){
            //For each item
            do{
                //Create the Instructor course and add it to the list
                courses.add(new StudentCourse(
                        getInt(cursor, StudentCourseEntry.CLOUD_ID),
                        getString(cursor, StudentCourseEntry.NAME),
                        getString(cursor, StudentCourseEntry.TIME),
                        getString(cursor, StudentCourseEntry.INSTRUCTOR)
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
