package org.tndata.officehours.util;

import android.content.Context;
import android.support.annotation.NonNull;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.model.ResultSet;
import org.tndata.officehours.parser.Parser;

import es.sandwatch.httprequests.HttpRequest;
import es.sandwatch.httprequests.HttpRequestError;


/**
 * Class to sync up all the necessary data in the background. For now works one way.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class DataSynchronizer implements HttpRequest.RequestCallback, Parser.ParserCallback{
    private static final String TAG = "DataSynchronizer";


    /**
     * Starts the synchronization process.
     *
     * @param context a reference to the context.
     */
    public static void sync(@NonNull Context context){
        new DataSynchronizer(context);
    }


    //Reference to the app
    private OfficeHoursApp application;

    //Things we need to update
    private int getProfileRC;
    private int getCoursesRC;


    private DataSynchronizer(@NonNull Context context){
        application = (OfficeHoursApp)context.getApplicationContext();

        getCoursesRC = HttpRequest.get(this, API.URL.courses());
    }


    @Override
    public void onRequestComplete(int requestCode, String result){
        if (requestCode == getCoursesRC){

        }
    }

    @Override
    public void onRequestFailed(int requestCode, HttpRequestError error){

    }

    @Override
    public void onProcessResult(int requestCode, ResultSet result){

    }

    @Override
    public void onParseSuccess(int requestCode, ResultSet result){

    }

    @Override
    public void onParseFailed(int requestCode){

    }
}
