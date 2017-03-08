package org.tndata.officehours.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Model class for a user.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class User extends Base{
    private static final String PREFERENCE_FILE = "OfficeHoursUserPreferences";


    @SerializedName("profile_id")
    private long profileId;

    //Fields retrieved from the API
    @SerializedName("email")
    private String email;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("google_image")
    private String photoUrl;
    private String schoolEmail;
    @SerializedName("phone")
    private String phoneNumber;
    @SerializedName("needs_onboarding")
    private boolean needsOnBoarding;

    @SerializedName("student")
    private boolean isStudent;

    @SerializedName("token")
    private String token;

    private List<String> officeHours;


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
        needsOnBoarding = false;
    }

    public void asStudent(){
        isStudent = true;
    }

    public void asTeacher(){
        isStudent = false;
    }

    public long getProfileId(){
        return profileId;
    }

    public String getEmail(){
        return email;
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
        return !needsOnBoarding;
    }

    public boolean isStudent(){
        return isStudent;
    }

    public String getToken(){
        return token;
    }

    public boolean is(Person person){
        return getId() == person.getId();
    }

    @Override
    public String toString(){
        String result = "User #" + getId() + ": " + firstName + " " + lastName;
        result += " (" + email +", " + phoneNumber + "). ";
        if (needsOnBoarding){
            result += "Needs on-boarding.";
        }
        else{
            result += "Doesn't need on-boarding.";
        }
        return result;
    }

    //START: User persistence in Shared Preferences

    /**
     * Writes the user to shared preferences. It replaces previously stored data.
     *
     * @param context a reference to the context.
     */
    public void writeToPreferences(@NonNull Context context){
        context = context.getApplicationContext();
        SharedPreferences user = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = user.edit();

        editor.putLong("user.id", getId());
        editor.putLong("user.profile_id", profileId);
        editor.putString("user.email", email);
        editor.putString("user.firstName", firstName);
        editor.putString("user.lastName", lastName);
        editor.putString("user.photoUrl", photoUrl);
        editor.putString("user.schoolEmail", schoolEmail);
        editor.putString("user.phoneNumber", phoneNumber);
        editor.putBoolean("user.needsOnBoarding", needsOnBoarding);
        editor.putBoolean("user.isStudent", isStudent);

        editor.putString("user.token", token);

        //NOTE: account type will be null before going through on boarding
        if (officeHours != null){
            String officeHoursCsv = "";
            for (String officeHour:officeHours){
                officeHoursCsv += officeHour + ",";
            }
            if (!officeHoursCsv.isEmpty()){
                officeHoursCsv = officeHoursCsv.substring(0, officeHoursCsv.length() - 2);
            }
            editor.putString("user.officeHours", officeHoursCsv);
        }

        editor.commit();
    }

    /**
     * Deletes all the information in the preference file.
     *
     * @param context a reference to the context.
     */
    public static void deleteFromPreferences(@NonNull Context context){
        context = context.getApplicationContext();
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
        context = context.getApplicationContext();
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
        super(preferences.getLong("user.id", -1));
        profileId = preferences.getLong("user.profile_id", -1);
        email = preferences.getString("user.email", "");
        firstName = preferences.getString("user.firstName", "");
        lastName = preferences.getString("user.lastName", "");
        photoUrl = preferences.getString("user.photoUrl", "");
        schoolEmail = preferences.getString("user.schoolEmail", "");
        phoneNumber = preferences.getString("user.phoneNumber", "");
        needsOnBoarding = preferences.getBoolean("user.needsOnBoarding", true);
        isStudent = preferences.getBoolean("user.isStudent", true);

        token = preferences.getString("user.token", "");

        String officeHoursCsv = preferences.getString("user.officeHours", "");
        if (!officeHoursCsv.isEmpty()){
            officeHours = new ArrayList<>(Arrays.asList(officeHoursCsv.split(",")));
        }
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        super.writeToParcel(parcel, flags);
        //TODO: not needed at the moment
    }

    public static final Creator<User> CREATOR = new Creator<User>(){
        @Override
        public User createFromParcel(Parcel source){
            return new User(source);
        }

        @Override
        public User[] newArray(int size){
            return new User[size];
        }
    };

    private User(Parcel src){
        super(src);
        //TODO: not needed at the moment
    }
}
