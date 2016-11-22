package org.tndata.officehours;

import android.app.Application;

import org.tndata.officehours.model.User;


/**
 * Application specific class.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class OfficeHoursApp extends Application{
    private User user;


    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }
}
