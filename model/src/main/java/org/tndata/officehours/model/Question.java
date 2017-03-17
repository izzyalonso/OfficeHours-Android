package org.tndata.officehours.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

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


    /**
     * Constructor.
     *
     * @param id the id of the question.
     * @param title the title of the question.
     * @param content the content of the question.
     * @param votes the number of positive votes of the question.
     * @param keywords the keywords of the question.
     */
    public Question(long id, @NonNull String title, @NonNull String content, int votes,
                    @NonNull List<String> keywords){

        super(id);
        this.title = title;
        this.content = content;
        this.votes = votes;
        this.keywords = keywords;
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public int getVotes(){
        return votes;
    }

    public List<String> getKeywords(){
        return keywords;
    }

    public String getKeywordCsv(){
        String result = "";
        for (String keyword:keywords){
            result += keyword + ",";
        }
        return result.substring(0, result.length()-1);
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
