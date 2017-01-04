package org.tndata.officehours.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.tndata.officehours.model.User;


/**
 * Created by ialonso on 1/4/17.
 */
public class API{
    public static class URL{
        public static String signIn(){
            return "https://staging.tndata.org/api/users/oauth/";
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
