package org.tndata.officehours.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import org.tndata.officehours.database.DatabaseContract.CourseEntry;
import org.tndata.officehours.model.Course;

import java.util.ArrayList;
import java.util.List;


/**
 * Table handler for courses.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class CourseTableHandler extends TableHandler{

    /*---------*
     * QUERIES *
     *---------*/

    static final String CREATE = "CREATE TABLE " + CourseEntry.TABLE + " ("
            + CourseEntry.ID + " INTEGER PRIMARY KEY, "
            + CourseEntry.CLOUD_ID + " INTEGER, "
            + CourseEntry.NAME + " TEXT, "
            + CourseEntry.LOCATION + " TEXT, "
            + CourseEntry.MEETING_TIME + " TEXT, "
            + CourseEntry.ACCESS_CODE + " TEXT)";

    private static final String INSERT = "INSERT INTO " + CourseEntry.TABLE + " ("
            + CourseEntry.CLOUD_ID + ", "
            + CourseEntry.NAME + ", "
            + CourseEntry.LOCATION + ", "
            + CourseEntry.MEETING_TIME + ", "
            + CourseEntry.ACCESS_CODE + ") "
            + "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE " + CourseEntry.TABLE + " SET "
            + CourseEntry.NAME + "=?, "
            + CourseEntry.LOCATION + "=?, "
            + CourseEntry.MEETING_TIME + "=?, "
            + CourseEntry.ACCESS_CODE + "=? "
            + "WHERE " + CourseEntry.CLOUD_ID + "=?";

    private static final String DELETE = "DELETE FROM " + CourseEntry.TABLE
            + " WHERE " + CourseEntry.CLOUD_ID + "=?";

    private static final String SELECT = "SELECT * FROM " + CourseEntry.TABLE;


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     */
    public CourseTableHandler(@NonNull Context context){
        init(context);
    }


    /*---------*
     * METHODS *
     *---------*/

    /**
     * Saves a Course to the database.
     *
     * @param course the course to be saved.
     */
    public void saveCourse(@NonNull Course course){
        //Open a connection to the database
        SQLiteDatabase db = getDatabase();

        //Prepare the statement
        SQLiteStatement stmt = db.compileStatement(INSERT);
        stmt.bindLong(1, course.getId());
        stmt.bindString(2, course.getName());
        stmt.bindString(3, course.getLocation());
        stmt.bindString(4, course.getMeetingTime());
        stmt.bindString(5, course.getAccessCode());

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
    public void saveCourses(@NonNull List<Course> courses){
        //Retrieve a database, begin the transaction, and compile the query
        SQLiteDatabase db = getDatabase();
        db.beginTransaction();
        SQLiteStatement stmt = db.compileStatement(INSERT);

        for (Course course:courses){
            //Previous bindings (if any) are emptied
            stmt.clearBindings();

            //Bindings
            stmt.bindLong(1, course.getId());
            stmt.bindString(2, course.getName());
            stmt.bindString(3, course.getLocation());
            stmt.bindString(4, course.getMeetingTime());
            stmt.bindString(5, course.getAccessCode());

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
    public void updateCourse(@NonNull Course course){
        //Open a connection to the database
        SQLiteDatabase db = getDatabase();

        //Prepare the statement
        SQLiteStatement stmt = db.compileStatement(UPDATE);
        stmt.bindString(1, course.getName());
        stmt.bindString(2, course.getLocation());
        stmt.bindString(3, course.getMeetingTime());
        stmt.bindString(4, course.getAccessCode());
        stmt.bindLong(5, course.getId());

        //Execute the query
        stmt.executeUpdateDelete();

        //Close up
        stmt.close();
    }

    /**
     * Deletes a course from the database.
     *
     * @param course the course to be deleted.
     */
    public void deleteCourse(@NonNull Course course){
        SQLiteDatabase db = getDatabase();

        SQLiteStatement stmt = db.compileStatement(DELETE);
        stmt.bindLong(1, course.getId());
        stmt.executeUpdateDelete();

        stmt.close();
    }

    /**
     * Fetches the courses stored in the database.
     *
     * @return the list of courses currently stored in the database.
     */
    public ArrayList<Course> getCourses(){
        //Open a readable database and execute the query
        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        ArrayList<Course> courses = new ArrayList<>();

        //If there are rows in the cursor returned by the query
        if (cursor.moveToFirst()){
            //For each item
            do{
                //Create the Instructor course and add it to the list
                courses.add(new Course(
                        getLong(cursor, CourseEntry.CLOUD_ID),
                        getString(cursor, CourseEntry.NAME),
                        getString(cursor, CourseEntry.LOCATION),
                        getString(cursor, CourseEntry.MEETING_TIME),
                        getString(cursor, CourseEntry.ACCESS_CODE)
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
