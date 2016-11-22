package org.tndata.officehours.model;


/**
 * Model class for a user.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class User{
    //Fields retrieved from Google
    private String email;
    private String googleToken;

    //Fields set during on boarding, some of these are pre-populated with Google's
    private AccountType accountType;
    private String schoolEmail;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean isOnBoardingComplete;

    //Fields retrieved from the API
    private String token;


    enum AccountType{
        STUDENT, TEACHER
    }
}
