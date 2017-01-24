package org.tndata.officehours.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import org.tndata.officehours.database.DatabaseContract.PersonEntry;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.Person;

import java.util.ArrayList;
import java.util.List;


/**
 * Handler for the Person table.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class PersonTableHandler extends TableHandler{

    /*---------*
     * QUERIES *
     *---------*/

    static final String CREATE = "CREATE TABLE " + PersonEntry.TABLE + " ("
            + PersonEntry.ID + " INTEGER PRIMARY KEY, "
            + PersonEntry.CLOUD_ID + " INTEGER, "
            + PersonEntry.COURSE_ID + " INTEGER, "
            + PersonEntry.NAME + " TEXT, "
            + PersonEntry.AVATAR + " TEXT, "
            + PersonEntry.IS_INSTRUCTOR + " INTEGER)";

    private static final String INSERT = "INSERT INTO " + PersonEntry.TABLE + " ("
            + PersonEntry.CLOUD_ID + ", "
            + PersonEntry.COURSE_ID + ", "
            + PersonEntry.NAME + ", "
            + PersonEntry.AVATAR + ", "
            + PersonEntry.IS_INSTRUCTOR + ") "
            + "VALUES (?, ?, ?, ?, ?)";

    private static final String DELETE_COURSE = "DELETE FROM " + PersonEntry.TABLE
            + " WHERE " + PersonEntry.COURSE_ID + "=?";

    private static final String DELETE = "DELETE FROM " + PersonEntry.TABLE
            + " WHERE " + PersonEntry.CLOUD_ID + "=?"
            + " AND " + PersonEntry.COURSE_ID + "=?";

    private static final String SELECT = "SELECT * FROM "
            + PersonEntry.TABLE
            + " WHERE " + PersonEntry.COURSE_ID + "=?";


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     */
    public PersonTableHandler(@NonNull Context context){
        init(context);
    }


    /*---------*
     * METHODS *
     *---------*/

    /**
     * Saves a Person to the database.
     *
     * @param person the person to be saved.
     */
    public void savePerson(@NonNull Person person, @NonNull Course course){
        //Open a connection to the database
        SQLiteDatabase db = getDatabase();

        //Prepare the statement
        SQLiteStatement stmt = db.compileStatement(INSERT);
        stmt.bindLong(1, person.getId());
        stmt.bindLong(2, course.getId());
        stmt.bindString(3, person.getName());
        stmt.bindString(4, person.getAvatar());
        stmt.bindLong(5, person.isInstructor() ? 1 : 0);

        //Execute the query
        stmt.executeInsert();

        //Close up
        stmt.close();
    }

    /**
     * Bulk-saves a list of people to the database.
     *
     * @param people the list of people to be saved.
     */
    public void savePeople(@NonNull List<Person> people, @NonNull Course course){
        //Retrieve a database, begin the transaction, and compile the query
        SQLiteDatabase db = getDatabase();
        db.beginTransaction();
        SQLiteStatement stmt = db.compileStatement(INSERT);

        for (Person person:people){
            //Previous bindings (if any) are emptied
            stmt.clearBindings();

            //Bindings
            stmt.bindLong(1, person.getId());
            stmt.bindLong(2, course.getId());
            stmt.bindString(3, person.getName());
            stmt.bindString(4, person.getAvatar());
            stmt.bindLong(5, person.isInstructor() ? 1 : 0);

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
     * Deletes all the people enrolled in a course in the database.
     *
     * @param course the course to delete the people from.
     */
    public void deletePeople(@NonNull Course course){
        SQLiteDatabase db = getDatabase();

        SQLiteStatement stmt = db.compileStatement(DELETE_COURSE);
        stmt.bindLong(1, course.getId());
        stmt.executeUpdateDelete();

        stmt.close();
    }

    /**
     * Deletes a single person from the database.
     *
     * @param person the person to be deleted.
     * @param course the course from which the person is to be deleted.
     */
    public void deletePerson(@NonNull Person person, @NonNull Course course){
        SQLiteDatabase db = getDatabase();

        SQLiteStatement stmt = db.compileStatement(DELETE);
        stmt.bindLong(1, person.getId());
        stmt.bindLong(2, course.getId());
        stmt.executeUpdateDelete();

        stmt.close();
    }

    /**
     * Fetches the list of people enrolled in a course stored in the database.
     *
     * @param course the course whose people are to be fetched.
     * @return the list of people enrolled in a course stored in the database.
     */
    public ArrayList<Person> getPeople(@NonNull Course course){
        //Open a readable database and execute the query
        SQLiteDatabase db = getDatabase();
        String[] selectionArgs = new String[]{course.getId() + ""};
        Cursor cursor = db.rawQuery(SELECT, selectionArgs);

        ArrayList<Person> people = new ArrayList<>();

        //If there are rows in the cursor returned by the query
        if (cursor.moveToFirst()){
            //For each item
            do{
                //Create the Instructor course and schedule_instructor it to the list
                people.add(new Person(
                        getInt(cursor, PersonEntry.CLOUD_ID),
                        getString(cursor, PersonEntry.NAME),
                        getString(cursor, PersonEntry.AVATAR),
                        getLong(cursor, PersonEntry.IS_INSTRUCTOR) == 1
                ));
            }
            //Move on until the cursor is empty
            while (cursor.moveToNext());
        }

        //Close both, the cursor and the database
        cursor.close();

        return people;
    }
}
