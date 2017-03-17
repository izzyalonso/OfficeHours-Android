package org.tndata.officehours.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.util.TypedValue;

import com.google.gson.JsonObject;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.Message;
import org.tndata.officehours.model.OfficeHours;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.model.Question;


/**
 * Class with utility functions.
 *
 * @author Ismael Alonso
 */
public class Util{
    public static Class getTypeOf(String type){
        return Course.class;
    }

    public static Class getTypeOf(JsonObject object){
        if (object.has("digest")){
            return Message.class;
        }
        else if (object.has("avatar")){
            return Person.class;
        }
        else if (object.has("votes")){
            return Question.class;
        }
        else if (object.has("from") && object.has("to")){
            return OfficeHours.TimeSlot.class;
        }
        return Course.class;
    }

    /**
     * Tells whether there is a network connection available.
     *
     * @param context a reference to the context.
     * @return true if there is a network connection, false otherwise.
     */
    public static boolean isNetworkAvailable(@NonNull Context context){
        String svc = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(svc);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Px
    public static int getPixels(Context context, int densityPixels){
        return (int)Math.ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, densityPixels,
                context.getResources().getDisplayMetrics()));
    }
}
