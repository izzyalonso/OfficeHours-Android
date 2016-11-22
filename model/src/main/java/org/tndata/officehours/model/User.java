package org.tndata.officehours.model;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

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


    /**
     * Constructor. Called when a user signs in with Google.
     *
     * @param account the result of signing in with Google.
     */
    public User(GoogleSignInAccount account){
        email = account.getEmail();
        googleToken = account.getIdToken();
        accountType = null; //Unknown yet
        schoolEmail = email;
        firstName = account.getGivenName();
        lastName = account.getFamilyName();
        phoneNumber = "";
        isOnBoardingComplete = false;
        token = null;
    }

    public void setAsStudent(){
        accountType = AccountType.STUDENT;
    }

    public void setAsTeacher(){
        accountType = AccountType.TEACHER;
    }

    public void setSchoolEmail(String schoolEmail){
        this.schoolEmail = schoolEmail;
    }

    public void setName(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void onBoardingCompleted(){
        isOnBoardingComplete = true;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getEmail(){
        return email;
    }

    public String getGoogleToken(){
        return googleToken;
    }

    public boolean isStudent(){
        return accountType == AccountType.STUDENT;
    }

    public boolean isTeacher(){
        return accountType == AccountType.TEACHER;
    }

    public String getSchoolEmail(){
        return schoolEmail;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public boolean isOnBoardingComplete(){
        return isOnBoardingComplete;
    }

    public String getToken(){
        return token;
    }


    enum AccountType{
        STUDENT, TEACHER
    }
}
