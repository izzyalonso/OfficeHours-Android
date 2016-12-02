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
            + CourseEntry.CODE + " TEXT, "
            + CourseEntry.NAME + " TEXT, "
            + CourseEntry.TIME + " TEXT, "
            + CourseEntry.EXPIRATION_DATE + " TEXT, "
            + CourseEntry.ACCESS_CODE + " TEXT, "
            + CourseEntry.INSTRUCTOR + " TEXT)";

    private static final String INSERT = "INSERT INTO " + CourseEntry.TABLE + " ("
            + CourseEntry.CLOUD_ID + ", "
            + CourseEntry.CODE + ", "
            + CourseEntry.NAME + ", "
            + CourseEntry.TIME + ", "
            + CourseEntry.EXPIRATION_DATE + ", "
            + CourseEntry.ACCESS_CODE + ", "
            + CourseEntry.INSTRUCTOR + ") "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE " + CourseEntry.TABLE + " SET "
            + CourseEntry.CODE + "=?, "
            + CourseEntry.NAME + "=?, "
            + CourseEntry.TIME + "=?, "
            + CourseEntry.EXPIRATION_DATE + "=?, "
            + CourseEntry.ACCESS_CODE + "=?, "
            + CourseEntry.INSTRUCTOR + "=? "
            + "WHERE " + CourseEntry.CLOUD_ID + "=?";

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
        stmt.bindString(2, course.getCode());
        stmt.bindString(3, course.getName());
        stmt.bindString(4, course.getMeetingTime());
        stmt.bindString(5, course.getExpirationDate());
        stmt.bindString(6, course.getAccessCode());
        stmt.bindString(7, course.getInstructorName());

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
            stmt.bindString(2, course.getCode());
            stmt.bindString(3, course.getName());
            stmt.bindString(4, course.getMeetingTime());
            stmt.bindString(5, course.getExpirationDate());
            stmt.bindString(6, course.getAccessCode());
            stmt.bindString(7, course.getInstructorName());

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
        stmt.bindString(1, course.getCode());
        stmt.bindString(2, course.getName());
        stmt.bindString(3, course.getMeetingTime());
        stmt.bindString(4, course.getExpirationDate());
        stmt.bindString(5, course.getAccessCode());
        stmt.bindString(6, course.getInstructorName());
        stmt.bindLong(7, course.getId());

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
                        getInt(cursor, CourseEntry.CLOUD_ID),
                        getString(cursor, CourseEntry.CODE),
                        getString(cursor, CourseEntry.NAME),
                        getString(cursor, CourseEntry.TIME),
                        getString(cursor, CourseEntry.EXPIRATION_DATE)
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
