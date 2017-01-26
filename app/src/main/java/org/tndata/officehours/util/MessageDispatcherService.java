package org.tndata.officehours.util;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.tndata.officehours.database.PersonTableHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by isma on 1/25/17.
 */
public class MessageDispatcherService extends Service implements WebSocketClient.Listener{
    private static final String TAG = "MessageDispatcherSvc";

    //Commands
    private static final String COMMAND_KEY = "org.tndata.officehours.MessageDispatchSvc.Command";
    private static final String START = "start";
    private static final String UPDATE = "update";
    private static final String KILL = "kill";


    public static void start(@NonNull Context context){
        Log.i(TAG, "Start request. Service will start shortly.");
        context.startService(new Intent(context, MessageDispatcherService.class)
                .putExtra(COMMAND_KEY, START));
    }

    public static void updateDataSet(@NonNull Context context){
        Log.i(TAG, "Update request. Service's data set will update shortly.");
        context.startService(new Intent(context, MessageDispatcherService.class)
                .putExtra(COMMAND_KEY, UPDATE));
    }

    public static void kill(@NonNull Context context){
        Log.i(TAG, "Kill request. Service will stop shortly.");
        context.startService(new Intent(context, MessageDispatcherService.class)
                .putExtra(COMMAND_KEY, KILL));
    }


    private boolean running;
    private boolean loadingData;
    private boolean dataSetLoaded;

    private static Map<Long, WebSocketClient> clientMap;


    @Override
    @SuppressLint("sparse")
    public void onCreate(){
        super.onCreate();
        running = false;
        loadingData = false;
        dataSetLoaded = false;
        clientMap = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if (intent == null){
            start(getApplicationContext());
        }
        else{
            switch (intent.getExtras().getString(COMMAND_KEY, "")){
                case START:
                    if (!running){
                        running = true;
                        loadData();
                    }
                    break;

                case UPDATE:

                    break;

                case KILL:

                    break;

                default:
                    Log.w(TAG, "Unrecognized command");
                    if (!running){
                        stopSelf();
                    }
                    break;
            }
        }

        return START_STICKY;
    }

    private void loadData(){
        if (!loadingData){
            loadingData = true;
            new Loader(this).execute();
        }
    }

    private void onDataLoaded(List<Long> ids){
        loadingData = false;

    }

    @Override
    public void onConnect(){

    }

    @Override
    public void onMessage(String message){

    }

    @Override
    public void onMessage(byte[] data){

    }

    @Override
    public void onDisconnect(int code, String reason){

    }

    @Override
    public void onError(Exception error){

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }


    /**
     * Loads the necessary data for the service to start working in the background.
     *
     * @author Ismael Alonso
     */
    private class Loader extends AsyncTask<Void, Void, List<Long>>{
        private Context context;


        /**
         * Constructor.
         *
         * @param context a reference to the context.
         */
        private Loader(@NonNull Context context){
            this.context = context;
        }

        @Override
        protected List<Long> doInBackground(Void... params){
            PersonTableHandler handler = new PersonTableHandler(context);
            List<Long> ids = handler.getUniqueUserIds();
            handler.close();
            return ids;
        }

        @Override
        protected void onPostExecute(List<Long> ids){
            onDataLoaded(ids);
        }
    }
}
