package org.tndata.officehours.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import org.tndata.officehours.database.DatabaseContract.MessageEntry;
import org.tndata.officehours.model.Message;
import org.tndata.officehours.model.Person;

import java.util.ArrayList;
import java.util.List;


/**
 * Table handler for messages.
 *
 * @author Ismael Alonso
 */
public class MessageTableHandler extends TableHandler{

    /*---------*
     * QUERIES *
     *---------*/
    
    static final String CREATE = "CREATE TABLE " + MessageEntry.TABLE + " ("
            + MessageEntry.ID + " INTEGER PRIMARY KEY, "
            + MessageEntry.CLOUD_ID + " INTEGER, "
            + MessageEntry.SENDER_ID + " INTEGER, "
            + MessageEntry.TEXT + " TEXT, "
            + MessageEntry.IS_READ + " INTEGER, "
            + MessageEntry.TIMESTAMP + " INTEGER)";

    private static final String INSERT = "INSERT INTO " + MessageEntry.TABLE + " ("
            + MessageEntry.CLOUD_ID + ", "
            + MessageEntry.SENDER_ID + ", "
            + MessageEntry.TEXT + ", "
            + MessageEntry.IS_READ + ", "
            + MessageEntry.TIMESTAMP + ") "
            + "VALUES (?, ?, ?, ?, ?)";

    private static final String READ = "UPDATE " + MessageEntry.TABLE + " SET "
            + MessageEntry.IS_READ + "=1 "
            + "WHERE " + MessageEntry.SENDER_ID + "=?";


    private static final String SELECT = "SELECT * FROM " + MessageEntry.TABLE
            + " WHERE " + MessageEntry.SENDER_ID + "=?"
            + " AND " + MessageEntry.TIMESTAMP + "<?"
            + " ORDER BY " + MessageEntry.TIMESTAMP + "DESC"
            + " LIMIT 25";


    /**
     * Constructor.
     *
     * @param context a reference to the context.
     */
    public MessageTableHandler(@NonNull Context context){
        init(context);
    }


    /*---------*
     * METHODS *
     *---------*/

    /**
     * Saves a message in the database.
     *
     * @param message the message to be saved.
     */
    public void saveMessage(@NonNull Message message){
        //Open a connection to the database
        SQLiteDatabase db = getDatabase();

        //Prepare the statement
        SQLiteStatement stmt = db.compileStatement(INSERT);
        stmt.bindLong(1, message.getId());
        stmt.bindLong(2, message.getSenderId());
        stmt.bindString(3, message.getText());
        stmt.bindLong(4, message.isRead() ? 1 : 0);
        stmt.bindLong(5, message.getTimestamp());

        //Execure and close
        stmt.executeInsert();
        stmt.close();
    }

    /**
     * Bulk saves a list of messages to the database.
     *
     * @param messages the list of messages to be saved.
     */
    public void saveMessages(@NonNull List<Message> messages){
        //Retrieve a database, begin the transaction, and compile the query
        SQLiteDatabase db = getDatabase();
        db.beginTransaction();
        SQLiteStatement stmt = db.compileStatement(INSERT);

        for (Message message:messages){
            //Previous bindings (if any) are emptied
            stmt.clearBindings();

            //Bindings
            stmt.bindLong(1, message.getId());
            stmt.bindLong(2, message.getSenderId());
            stmt.bindString(3, message.getText());
            stmt.bindLong(4, message.isRead() ? 1 : 0);
            stmt.bindLong(5, message.getTimestamp());
        }

        //Close the transaction
        db.setTransactionSuccessful();
        db.endTransaction();

        //Close the database
        stmt.close();
    }

    /**
     * Marks all the messages from a person as read.
     *
     * @param sender the person who sent the messages.
     */
    public void readMessages(@NonNull Person sender){
        //Open a connection to the database
        SQLiteDatabase db = getDatabase();

        //Prepare the statement
        SQLiteStatement stmt = db.compileStatement(READ);
        stmt.bindLong(1, sender.getId());
        //Execute the query
        stmt.executeUpdateDelete();

        //Close up
        stmt.close();
    }

    /**
     * Reads the last 25 messages from a person before a given timestamp.
     *
     * @param sender the person who sent the messages.
     * @param beforeTimestamp fetch only messages before this timestamp.
     * @return the requested list of messages.
     */
    public ArrayList<Message> getMessages(@NonNull Person sender, long beforeTimestamp){
        //Open a readable database and execute the query
        SQLiteDatabase db = getDatabase();
        String[] selectionArgs = new String[]{sender.getId() + "", beforeTimestamp + ""};
        Cursor cursor = db.rawQuery(SELECT, selectionArgs);

        ArrayList<Message> messages = new ArrayList<>();

        //If there are rows in the cursor returned by the query
        if (cursor.moveToFirst()){
            //For each item
            do{
                Message message = new Message(
                        getLong(cursor, MessageEntry.CLOUD_ID),
                        getLong(cursor, MessageEntry.SENDER_ID),
                        getString(cursor, MessageEntry.TEXT),
                        getLong(cursor, MessageEntry.TIMESTAMP)
                );
                if (getLong(cursor, MessageEntry.IS_READ) == 1){
                    message.read();
                }
                messages.add(message);
            }
            //Move on until the cursor is empty
            while (cursor.moveToNext());
        }
        return messages;
    }
}
