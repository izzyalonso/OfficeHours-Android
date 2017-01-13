package org.tndata.officehours.util;

import com.google.gson.JsonObject;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.Person;


/**
 * Created by ialonso on 1/9/17.
 */
public class Util{
    public static Class getTypeOf(String type){
        return Course.class;
    }

    public static Class getTypeOf(JsonObject object){
        if (object.has("avatar")){
            return Person.class;
        }
        return Course.class;
    }
}
