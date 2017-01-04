package org.tndata.officehours.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Model class for a user.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class User{
    private static final String PREFERENCE_FILE = "OfficeHoursUserPreferences";

    //Fields retrieved from Google
    private String email;
    private String googleToken;

    //Fields set during on boarding, some of these are pre-populated with Google's
    private AccountType accountType;
    private List<String> officeHours;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private String schoolEmail;
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
        //googleToken = account.getServerAuthCode();
        googleToken = account.getIdToken();
        accountType = null; //Unknown yet
        firstName = account.getGivenName();
        lastName = account.getFamilyName();
        Uri url = account.getPhotoUrl();
        photoUrl = url == null ? "" : account.getPhotoUrl().toString();
        schoolEmail = email;
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

    public void setOfficeHours(List<String> officeHours){
        this.officeHours = officeHours;
    }

    public void setName(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setSchoolEmail(String schoolEmail){
        this.schoolEmail = schoolEmail;
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

    public boolean hasDefinedType(){
        return accountType != null;
    }

    public boolean isStudent(){
        return accountType == AccountType.STUDENT;
    }

    public boolean isTeacher(){
        return accountType == AccountType.TEACHER;
    }

    public List<String> getOfficeHours(){
        return officeHours;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getPhotoUrl(){
        return photoUrl;
    }

    public String getName(){
        return firstName + " " + lastName;
    }

    public String getSchoolEmail(){
        return schoolEmail;
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


    //START: User persistence in Shared Preferences

    /**
     * Writes the user to shared preferences. It replaces previously stored data.
     *
     * @param context a reference to the context.
     */
    public void writeToPreferences(@NonNull Context context){
        SharedPreferences user = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = user.edit();

        editor.putString("user.email", email);
        editor.putString("user.googleToken", googleToken);

        //NOTE: account type will be null before going through on boarding
        if (accountType == null){
            editor.putString("user.accountType", "");
        }
        else{
            editor.putString("user.accountType", accountType.getDescriptor());
            if (accountType == AccountType.TEACHER && officeHours != null){
                String officeHoursCsv = "";
                for (String officeHour:officeHours){
                    officeHoursCsv += officeHour + ",";
                }
                if (!officeHoursCsv.isEmpty()){
                    officeHoursCsv = officeHoursCsv.substring(0, officeHoursCsv.length() - 2);
                }
                editor.putString("user.officeHours", officeHoursCsv);
            }
        }
        editor.putString("user.firstName", firstName);
        editor.putString("user.lastName", lastName);
        editor.putString("user.photoUrl", photoUrl);
        editor.putString("user.schoolEmail", schoolEmail);
        editor.putString("user.phoneNumber", phoneNumber);
        editor.putBoolean("user.isOnBoardingComplete", isOnBoardingComplete);

        editor.putString("user.token", token);
        editor.apply();
    }

    /**
     * Deletes all the information in the preference file.
     *
     * @param context a reference to the context.
     */
    public static void deleteFromPreferences(@NonNull Context context){
        context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE).edit().clear().commit();
    }

    /**
     * Reads the user stored in Shared Preferences, if any.
     *
     * @param context a reference to the context.
     * @return the user if one exists or null.
     */
    @Nullable
    public static User readFromPreferences(@NonNull Context context){
        //Open the shared preferences file for the user and check if they exist
        SharedPreferences user = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        String email = user.getString("user.email", null);
        if (email == null){
            //If not, return null
            return null;
        }
        return new User(user);
    }

    /**
     * Internal constructor. To be used when reading from preferences.
     *
     * @param preferences the preference map to read the user from.
     */
    private User(SharedPreferences preferences){
        email = preferences.getString("user.email", "");
        googleToken = preferences.getString("user.googleToken", "");

        accountType = AccountType.getAccountType(preferences.getString("user.accountType", ""));
        if (accountType == AccountType.TEACHER){
            String officeHoursCsv = preferences.getString("user.officeHours", "");
            if (!officeHoursCsv.isEmpty()){
                officeHours = new ArrayList<>(Arrays.asList(officeHoursCsv.split(",")));
            }
        }
        firstName = preferences.getString("user.firstName", "");
        lastName = preferences.getString("user.lastName", "");
        photoUrl = preferences.getString("user.photoUrl", "");
        schoolEmail = preferences.getString("user.schoolEmail", "");
        phoneNumber = preferences.getString("user.phoneNumber", "");
        isOnBoardingComplete = preferences.getBoolean("user.isOnBoardingComplete", false);

        token = preferences.getString("user.token", "");
    }


    /**
     * Types of accounts supported by the platform.
     *
     * @author Ismael Alonso
     * @version 1.0.0
     */
    private enum AccountType{
        STUDENT("student"), TEACHER("teacher");


        private final String descriptor;


        /**
         * Constructor.
         *
         * @param descriptor the account type descriptor.
         */
        AccountType(String descriptor){
            this.descriptor = descriptor;
        }

        /**
         * Descriptor getter.
         *
         * @return the descriptor
         */
        private String getDescriptor(){
            return descriptor;
        }

        /**
         * Gets an account type for a given descriptor. In order for this method to return an
         * actual account type the match must be exact.
         *
         * @param descriptor the descriptor to be matched.
         * @return the account type mapped to the provided descriptor or null no mapping was found.
         */
        @Nullable
        private static AccountType getAccountType(@Nullable String descriptor){
            if (descriptor == null){
                return null;
            }
            if (descriptor.equals("student")){
                return STUDENT;
            }
            if (descriptor.equals("teacher")){
                return TEACHER;
            }
            return null;
        }
    }
}
