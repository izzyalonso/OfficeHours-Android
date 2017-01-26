package org.tndata.officehours.parser;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.tndata.officehours.parser.deserializer.ListDeserializer;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Contains the methods used by the Parser to generate content out of a JSON string.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public abstract class ParserMethods{
    public static final Gson sGson = new GsonBuilder()
            .registerTypeAdapter(List.class, new ListDeserializer())
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .create();
}
