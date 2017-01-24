package org.tndata.officehours.parser.deserializer;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.tndata.officehours.parser.ParserMethods;
import org.tndata.officehours.util.Util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Generic deserializer for Lists. Converts JsonArrays to Lists (ArrayList).
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class ListDeserializer implements JsonDeserializer<List<?>>{
    @Override
    public List<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context){
        return parse(json);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> parse(JsonElement item){
        //Create the list where the parsed objects will be put
        List<T> list = new ArrayList<>();

        //We've got some JSONArrays that come as JSONObjects from the api, skip
        //  those to return an empty List, which is what that represents. That
        //  is not regular expected behavior though
        if (!item.toString().equals("{}")){
            //Parse all the elements of the array and schedule_instructor them to the list
            for (JsonElement element:item.getAsJsonArray()){
                try{
                    Log.d("ListDeserializer", element.toString());
                    list.add((T)ParserMethods.sGson.fromJson(element, Util.getTypeOf((JsonObject)element)));
                }
                catch (ClassCastException ccx){
                    list.add((T)ParserMethods.sGson.fromJson(element, Long.class));
                    Log.d("LongList", list.size()+"");
                }
            }
        }

        return list;
    }
}
