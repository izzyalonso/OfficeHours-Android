package org.tndata.officehours.database;

import android.content.Context;
import android.support.annotation.NonNull;
import org.tndata.officehours.database.DatabaseContract.QuestionEntry;


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


    public QuestionsTableHandler(@NonNull Context context){
        init(context);
    }


}
