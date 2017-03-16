package org.tndata.officehours.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.tndata.officehours.OfficeHoursApp;
import org.tndata.officehours.util.Util;


/**
 * Receiver to check network connectivity.
 *
 * @author Ismael Alonso.
 */
public class ConnectivityStateReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){
        OfficeHoursApp app = (OfficeHoursApp)context.getApplicationContext();
        app.connectionStateChanged(Util.isNetworkAvailable(context.getApplicationContext()));
    }
}
