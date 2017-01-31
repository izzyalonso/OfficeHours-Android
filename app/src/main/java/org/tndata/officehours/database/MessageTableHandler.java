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
            + MessageEntry.SENDER_ID + " INTEGER, "
            + MessageEntry.RECIPIENT_ID + " INTEGER, "
            + MessageEntry.TEXT + " TEXT, "
            + MessageEntry.TIMESTAMP + " INTEGER, "
            + MessageEntry.IS_READ + " INTEGER)";

    private static final String INSERT = "INSERT INTO " + MessageEntry.TABLE + " ("
            + MessageEntry.SENDER_ID + ", "
            + MessageEntry.RECIPIENT_ID + ", "
            + MessageEntry.TEXT + ", "
            + MessageEntry.TIMESTAMP + ", "
            + MessageEntry.IS_READ + ") "
            + "VALUES (?, ?, ?, ?, ?)";

    private static final String READ = "UPDATE " + MessageEntry.TABLE + " SET "
            + MessageEntry.IS_READ + "=1 "
            + "WHERE " + MessageEntry.RECIPIENT_ID + "=?";


    private static final String SELECT = "SELECT * FROM " + MessageEntry.TABLE
            + " WHERE (" + MessageEntry.SENDER_ID + "=? OR " + MessageEntry.RECIPIENT_ID + "=?)"
            + " AND " + MessageEntry.TIMESTAMP + "<?"
            + " ORDER BY " + MessageEntry.TIMESTAMP + " DESC"
            + " LIMIT 25";

    private static final String LAST_TIMESTAMP = "SELECT " + MessageEntry.TIMESTAMP
            + " FROM " + MessageEntry.TABLE
            + " WHERE " + MessageEntry.SENDER_ID + "=? OR " + MessageEntry.RECIPIENT_ID + "=?"
            + " ORDER BY " + MessageEntry.TIMESTAMP + " DESC"
            + " LIMIT 1";


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
        stmt.bindLong(1, message.getSenderId());
        stmt.bindLong(2, message.getRecipientId());
        stmt.bindString(3, message.getText());
        stmt.bindLong(4, message.getTimestamp());
        stmt.bindLong(5, message.isRead() ? 1 : 0);

        //Execute and close
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
            stmt.bindLong(1, message.getSenderId());
            stmt.bindLong(2, message.getRecipientId());
            stmt.bindString(3, message.getText());
            stmt.bindLong(4, message.getTimestamp());
            stmt.bindLong(5, message.isRead() ? 1 : 0);
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
     * @param recipient the person who sent the messages.
     */
    public void readMessages(@NonNull Person recipient){
        //Open a connection to the database
        SQLiteDatabase db = getDatabase();

        //Prepare the statement
        SQLiteStatement stmt = db.compileStatement(READ);
        stmt.bindLong(1, recipient.getId());
        //Execute the query
        stmt.executeUpdateDelete();

        //Close up
        stmt.close();
    }

    /**
     * Reads the last 25 messages in a conversation before a given timestamp.
     *
     * @param partner the person who sent the messages.
     * @param beforeTimestamp fetch only messages before this timestamp.
     * @return the requested list of messages.
     */
    public ArrayList<Message> getMessages(@NonNull Person partner, long beforeTimestamp){
        //Open a readable database and execute the query
        SQLiteDatabase db = getDatabase();
        String[] selectionArgs = new String[]{
                partner.getId() + "", partner.getId() + "", beforeTimestamp + ""
        };
        Cursor cursor = db.rawQuery(SELECT, selectionArgs);

        ArrayList<Message> messages = new ArrayList<>();

        //If there are rows in the cursor returned by the query, read them
        if (cursor.moveToFirst()){
            do{
                Message message = new Message(
                        getLong(cursor, MessageEntry.SENDER_ID),
                        getLong(cursor, MessageEntry.RECIPIENT_ID),
                        getString(cursor, MessageEntry.TEXT),
                        getLong(cursor, MessageEntry.TIMESTAMP),
                        true
                );
                if (getLong(cursor, MessageEntry.IS_READ) == 1){
                    message.read();
                }
                messages.add(message);
            }
            //Move on until the cursor is empty
            while (cursor.moveToNext());
        }
        cursor.close();
        return messages;
    }

    public long getLastTimestamp(@NonNull Person partner){
        SQLiteDatabase db = getDatabase();
        String args[] = new String[]{partner.getId() + "", partner.getId() + ""};
        Cursor cursor = db.rawQuery(LAST_TIMESTAMP, args);

        if (cursor.moveToFirst()){
            long timestamp = getLong(cursor, MessageEntry.TIMESTAMP);
            cursor.close();
            return timestamp;
        }
        else{
            cursor.close();
            return 0;
        }
    }
}
