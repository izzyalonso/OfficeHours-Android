package org.tndata.officehours.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.tndata.officehours.BuildConfig;
import org.tndata.officehours.model.User;


/**
 * Created by ialonso on 1/4/17.
 */
public class API{
    //Api urls and app configuration
    public static final boolean STAGING = BuildConfig.DEBUG;
    private static final boolean USE_NGROK_TUNNEL = false;
    private static final String TNDATA_BASE_URL = "https://app.tndata.org/api/";
    private static final String TNDATA_STAGING_URL = "https://staging.tndata.org/api/";
    private static final String NGROK_TUNNEL_URL = "https://tndata.ngrok.io/api/";

    @SuppressWarnings("ConstantConditions")
    private static final String BASE_URL =
            USE_NGROK_TUNNEL ?
                    NGROK_TUNNEL_URL
                    :
                    STAGING ?
                            TNDATA_STAGING_URL
                            :
                            TNDATA_BASE_URL;


    private API(){

    }
    public static class URL{
        public static String signIn(){
            return BASE_URL + "users/oauth/";
        }

        public static String courses(){
            return BASE_URL + "courses/";
        }
    }

    public static class BODY{
        public static JSONObject signIn(User user){
            JSONObject body = new JSONObject();
            try{
                body.put("email", user.getEmail());
                body.put("first_name", user.getFirstName());
                body.put("last_name", user.getLastName());
                body.put("image_url", "");
                body.put("oauth_token", user.getGoogleToken());
            }
            catch (JSONException jx){
                jx.printStackTrace();
            }
            return body;
        }
    }
}
