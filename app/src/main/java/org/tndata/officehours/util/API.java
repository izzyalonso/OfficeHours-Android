package org.tndata.officehours.util;

import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;
import org.tndata.officehours.BuildConfig;
import org.tndata.officehours.model.Course;
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
         * @param userId the id of the user.
         * @return the url to the mentioned websocket.
         */
        public static String chatSocket(long userId){
            return SOCKET_URL + "chat/" + userId + "/";
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

        public static String chatMessage(@NonNull User user, @NonNull String text){
            JSONObject body = new JSONObject();
            try{
                body.put("text", text);
                body.put("from", user.getId());
                body.put("token", user.getToken());
            }
            catch (JSONException jx){
                jx.printStackTrace();
            }
            return body.toString();
        }
    }
}
