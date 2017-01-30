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
            + PersonEntry.LAST_MESSAGE + " TEXT)";

    private static final String INSERT = "INSERT INTO " + PersonEntry.TABLE + " ("
            + PersonEntry.CLOUD_ID + ", "
            + PersonEntry.COURSE_ID + ", "
            + PersonEntry.NAME + ", "
            + PersonEntry.AVATAR + ", "
            + PersonEntry.LAST_MESSAGE + ") "
            + "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE " + PersonEntry.TABLE + " SET "
            + PersonEntry.NAME + "=?, "
            + PersonEntry.AVATAR + "=?, "
            + PersonEntry.LAST_MESSAGE + "=? "
            + "WHERE " + PersonEntry.CLOUD_ID + "=?";

    private static final String DELETE_COURSE = "DELETE FROM " + PersonEntry.TABLE
            + " WHERE " + PersonEntry.COURSE_ID + "=?";

    private static final String DELETE = "DELETE FROM " + PersonEntry.TABLE
            + " WHERE " + PersonEntry.CLOUD_ID + "=?"
            + " AND " + PersonEntry.COURSE_ID + "=?";

    private static final String SELECT = "SELECT * FROM "
            + PersonEntry.TABLE
            + " WHERE " + PersonEntry.COURSE_ID + "=?";

    private static final String SELECT_DISTINCT_ID = "SELECT DISTINCT " + PersonEntry.CLOUD_ID
            + " FROM " + PersonEntry.TABLE;


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
        stmt.bindString(5, person.getLastMessage());

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
            stmt.bindString(5, person.getLastMessage());

            //Execution
            stmt.executeInsert();
        }

        //Close the transaction
        db.setTransactionSuccessful();
        db.endTransaction();

        //Close the statement
        stmt.close();
    }

    /**
     * Updates a person in the database.
     *
     * @param person the person to be updated.
     */
    public void updatePerson(@NonNull Person person){
        SQLiteDatabase db = getDatabase();

        SQLiteStatement stmt = db.compileStatement(UPDATE);
        stmt.bindString(1, person.getName());
        stmt.bindString(2, person.getAvatar());
        stmt.bindString(3, person.getLastMessage());
        stmt.bindLong(4, person.getId());
        stmt.executeUpdateDelete();

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
            //Populate the list of people
            do{
                people.add(new Person(
                        getInt(cursor, PersonEntry.CLOUD_ID),
                        getString(cursor, PersonEntry.NAME),
                        getString(cursor, PersonEntry.AVATAR),
                        getString(cursor, PersonEntry.LAST_MESSAGE)
                ));
            }
            //Move on until the cursor is empty
            while (cursor.moveToNext());
        }

        //Close the cursor
        cursor.close();

        return people;
    }

    /**
     * Gets a list of unique ids of people in the database.
     *
     * @return a list of ids.
     */
    public ArrayList<Long> getUniqueUserIds(){
        //Open a readable database and execute the query
        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.rawQuery(SELECT_DISTINCT_ID, null);

        ArrayList<Long> ids = new ArrayList<>();

        //If there are rows in the cursor returned by the query
        if (cursor.moveToFirst()){
            //For each item
            do{
                ids.add(getLong(cursor, PersonEntry.CLOUD_ID));
            }
            //Move on until the cursor is empty
            while (cursor.moveToNext());
        }

        //Close the cursor
        cursor.close();

        return ids;
    }
}
