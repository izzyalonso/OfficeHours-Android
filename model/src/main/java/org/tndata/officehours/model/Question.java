package org.tndata.officehours.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Model class for a question.
 *
 * @author Ismael Alonso
 */
public class Question extends Base{
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("votes")
    private int votes;
    @SerializedName("keywords")
    private List<String> keywords;


    public Question(long id){
        super(id);
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        super.writeToParcel(parcel, flags);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeInt(votes);
        parcel.writeStringList(keywords);
    }

    public static final Creator<Question> CREATOR = new Creator<Question>(){
        @Override
        public Question createFromParcel(Parcel in){
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size){
            return new Question[size];
        }
    };

    private Question(Parcel in){
        super(in);
        title = in.readString();
        content = in.readString();
        votes = in.readInt();
        keywords = new ArrayList<>();
        in.readStringList(keywords);
    }
}
