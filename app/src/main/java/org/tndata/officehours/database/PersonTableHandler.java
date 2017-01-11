package org.tndata.officehours.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import org.tndata.officehours.database.DatabaseContract.PersonEntry;
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

    private static final String DELETE_COURSE = "DELETE FROM "
            + PersonEntry.TABLE
            + " WHERE " + PersonEntry.COURSE_ID + "=?";

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
    public void savePerson(@NonNull Person person){
        //Open a connection to the database
        SQLiteDatabase db = getDatabase();

        //Prepare the statement
        SQLiteStatement stmt = db.compileStatement(INSERT);
        stmt.bindLong(1, person.getId());
        stmt.bindString(2, person.getName());
        stmt.bindString(3, person.getAvatar());
        stmt.bindLong(4, person.isInstructor() ? 1 : 0);

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
    public void savePeople(@NonNull List<Person> people){
        //Retrieve a database, begin the transaction, and compile the query
        SQLiteDatabase db = getDatabase();
        db.beginTransaction();
        SQLiteStatement stmt = db.compileStatement(INSERT);

        for (Person person:people){
            //Previous bindings (if any) are emptied
            stmt.clearBindings();

            //Bindings
            stmt.bindLong(1, person.getId());
            stmt.bindString(2, person.getName());
            stmt.bindString(3, person.getAvatar());
            stmt.bindLong(4, person.isInstructor() ? 1 : 0);

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
     * Fetches the list of people enrolled in a course stored in the database.
     *
     * @param courseId the id of the course.
     * @return the list of people enrolled in a course stored in the database.
     */
    public ArrayList<Person> getPeople(long courseId){
        //Open a readable database and execute the query
        SQLiteDatabase db = getDatabase();
        String[] selectionArgs = new String[]{courseId + ""};
        Cursor cursor = db.rawQuery(SELECT, selectionArgs);

        ArrayList<Person> people = new ArrayList<>();

        //If there are rows in the cursor returned by the query
        if (cursor.moveToFirst()){
            //For each item
            do{
                //Create the Instructor course and add it to the list
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
