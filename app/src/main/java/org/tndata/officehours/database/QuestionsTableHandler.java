package org.tndata.officehours.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;
import org.tndata.officehours.database.DatabaseContract.QuestionEntry;
import org.tndata.officehours.model.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Table handler or the list of questions.
 * @author Ismael Alonso
 */
public class QuestionsTableHandler extends TableHandler{

    static final String CREATE = "CREATE TABLE " + QuestionEntry.TABLE + " ("
            + QuestionEntry.ID + " INTEGER PRIMARY KEY, "
            + QuestionEntry.CLOUD_ID + " INTEGER, "
            + QuestionEntry.TITLE + " TEXT, "
            + QuestionEntry.CONTENT + " TEXT, "
            + QuestionEntry.VOTES + " INTEGER, "
            + QuestionEntry.KEYWORDS + " TEXT)";

    private static final String INSERT = "INSERT INTO " + QuestionEntry.TABLE + " ("
            + QuestionEntry.CLOUD_ID + ", "
            + QuestionEntry.TITLE + ", "
            + QuestionEntry.CONTENT + ", "
            + QuestionEntry.VOTES + ", "
            + QuestionEntry.KEYWORDS + ") "
            + "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT = "SELECT * FROM " + QuestionEntry.TABLE;

    private static final String TRUNCATE = "DELETE FROM " + QuestionEntry.TABLE;


    public QuestionsTableHandler(@NonNull Context context){
        init(context);
    }

    public void saveQuestions(List<Question> questions){
        SQLiteDatabase db = getDatabase();
        db.beginTransaction();
        SQLiteStatement stmt = db.compileStatement(INSERT);

        for (Question question:questions){
            //Previous bindings (if any) are emptied
            stmt.clearBindings();

            //Bindings
            stmt.bindLong(1, question.getId());
            stmt.bindString(2, question.getTitle());
            stmt.bindString(3, question.getContent());
            stmt.bindLong(4, question.getVotes());
            stmt.bindString(5, question.getKeywordCsv());

            //Execution
            stmt.executeInsert();
        }

        //Close the transaction
        db.setTransactionSuccessful();
        db.endTransaction();

        //Close the statement
        stmt.close();
    }

    public List<Question> getQuestions(){
        //Open a readable database and execute the query
        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        ArrayList<Question> questions = new ArrayList<>();

        //If there are rows in the cursor returned by the query
        if (cursor.moveToFirst()){
            //Populate the list of people
            do{
                List<String> keywords = Arrays.asList(getString(cursor, QuestionEntry.KEYWORDS).split(","));
                questions.add(new Question(
                        getLong(cursor, QuestionEntry.CLOUD_ID),
                        getString(cursor, QuestionEntry.TITLE),
                        getString(cursor, QuestionEntry.CONTENT),
                        getInt(cursor, QuestionEntry.VOTES),
                        keywords
                ));
            }
            //Move on until the cursor is empty
            while (cursor.moveToNext());
        }

        //Close the cursor
        cursor.close();

        return questions;
    }

    /**
     * Empties the table.
     */
    public void erase(){
        getDatabase().execSQL(TRUNCATE);
    }
}
