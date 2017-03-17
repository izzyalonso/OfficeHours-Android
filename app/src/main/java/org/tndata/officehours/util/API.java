package org.tndata.officehours.util;

import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;
import org.tndata.officehours.BuildConfig;
import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.Message;
import org.tndata.officehours.model.Person;
import org.tndata.officehours.model.User;


/**
 * Class containing generators for endpoint URLs and request bodies.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class API{
    //Api urls and app configuration
    public static final boolean STAGING = BuildConfig.DEBUG;
    private static final boolean USE_NGROK_TUNNEL = false;
    private static final String APP_API_URL = "https://app.tndata.org/api/";
    private static final String STAGING_API_URL = "https://staging.tndata.org/api/";
    private static final String NGROK_TUNNEL_URL = "https://tndata.ngrok.io/api/";

    @SuppressWarnings("ConstantConditions")
    private static final String API_URL =
            USE_NGROK_TUNNEL ?
                    NGROK_TUNNEL_URL
                    :
                    STAGING ?
                            STAGING_API_URL
                            :
                            APP_API_URL;


    private static final String APP_SOCKET_URL = "wss://app.tndata.org/";
    private static final String STAGING_SOCKET_URL = "wss://staging.tndata.org/";

    private static final String SOCKET_URL =
            STAGING ?
                    STAGING_SOCKET_URL
                    :
                    APP_SOCKET_URL;


    private API(){

    }

    /**
     * Class containing information about the API endpoint URLS.
     *
     * @author Ismael Alonso
     * @version 1.0.0
     */
    public static class URL{
        /**
         * Gets the endpoint to POST a user who is signing in.
         *
         * @return the named endpoint.
         */
        public static String signIn(){
            return API_URL + "users/oauth/";
        }

        public static String officeHours(){
            return API_URL + "officehours/";
        }

        /**
         * Gets the url to update the user's profile.
         *
         * @return the named endpoint.
         */
        public static String profile(@NonNull User user){
            return API_URL + "users/profile/" + user.getProfileId() + "/";
        }

        /**
         * Gets the API endpoint for GETting and POSTing courses.
         *
         * @return the named endpoint.
         */
        public static String courses(){
            return API_URL + "courses/";
        }

        /**
         * Gets the API endpoint for PUTting courses.
         *
         * @param id the id of the course to update.
         * @return the named endpoint.
         */
        public static String courses(long id){
            return API_URL + "courses/" + id + "/";
        }

        /**
         * Gets the API endpoint to enroll a user in a course.
         *
         * @return the named endpoint.
         */
        public static String courseEnroll(){
            return courses() + "enroll/";
        }

        /**
         * Gets the url to a websocket to chat with a user.
         *
         * @param recipient the person the socket is to be opened to.
         * @return the url to the mentioned websocket.
         */
        public static String chatSocket(@NonNull Person recipient){
            return SOCKET_URL + "chat/" + recipient.getId() + "/";
        }

        public static String chatHistorySince(long user1, long user2, String timestamp){
            long first = user1 < user2 ? user1 : user2;
            long second = user1 > user2 ? user1 : user2;
            String room = "room=chat-" + first + "-" + second;
            timestamp = timestamp.replace(" ", "%20");
            String time = "since=" + timestamp;
            return API_URL + "chat/history/?" + room + "&" + time;
        }

        public static String chatHistoryBefore(long user1, long user2, String timestamp){
            long first = user1 < user2 ? user1 : user2;
            long second = user1 > user2 ? user1 : user2;
            String room = "room=chat-" + first + "-" + second;
            timestamp = timestamp.replace(" ", "%20");
            String time = "before=" + timestamp;
            return API_URL + "chat/history/?" + room + "&" + time;
        }

        public static String questions(){
            return API_URL + "questions/";
        }
    }

    public static class BODY{
        public static JSONObject signIn(GoogleSignInAccount account){
            JSONObject body = new JSONObject();
            try{
                body.put("email", account.getEmail());
                body.put("first_name", account.getGivenName());
                body.put("last_name", account.getFamilyName());
                body.put("image_url", account.getPhotoUrl());
                body.put("oauth_token", account.getIdToken());
            }
            catch (JSONException jx){
                jx.printStackTrace();
            }
            return body;
        }

        public static JSONObject officeHours(@NonNull String slot){
            JSONObject body = new JSONObject();
            try{
                body.put("meetingtime", slot);
            }
            catch (JSONException jx){
                jx.printStackTrace();
            }
            return body;
        }

        public static JSONObject profile(@NonNull User user){
            JSONObject body = new JSONObject();
            try{
                body.put("first_name", user.getFirstName());
                body.put("last_name", user.getLastName());
                body.put("phone", user.getPhoneNumber());
                body.put("needs_onboarding", !user.isOnBoardingComplete());
            }
            catch (JSONException jx){
                jx.printStackTrace();
            }
            return body;
        }

        public static JSONObject postPutCourse(@NonNull Course course){
            JSONObject body = new JSONObject();
            try{
                //body.put("_code", course.getCode());
                body.put("name", course.getName());
                body.put("location", course.getLocation());
                body.put("meetingtime", course.getMeetingTime());
                //body.put("last_meeting_date", course.getLastMeetingDate());
            }
            catch (JSONException jx){
                jx.printStackTrace();
            }
            return body;
        }

        public static JSONObject courseEnroll(@NonNull String code){
            JSONObject body = new JSONObject();
            try{
                body.put("code", code);
            }
            catch (JSONException jx){
                jx.printStackTrace();
            }
            return body;
        }

        public static String chatMessage(@NonNull Message message){
            JSONObject body = new JSONObject();
            try{
                body.put("text", message.getText());
            }
            catch (JSONException jx){
                jx.printStackTrace();
            }
            return body.toString();
        }
    }
}
