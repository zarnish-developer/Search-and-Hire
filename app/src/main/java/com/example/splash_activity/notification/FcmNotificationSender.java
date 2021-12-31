package com.example.splash_activity.notification;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.splash_activity.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationSender {
    String userFcmToken;
    String title;
    String body;
    Context mcontext;
    Activity mActivity;

    private RequestQueue requestQueue;
    private final String postUrl="https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey="AAAAPExktjo:APA91bFyC5xdWr4u0waDPyiTODbhsFuhcM2bS3V7U8H7Aldi5Ax_aEAIxrvOsIv3HyQdRcD1ZJ7C5-Q8UsriBkaLlfNwK16RY7-rhi4DYqDq-WwR3rRKJMbJRIpfOQJW68aQl5xtxFDy";

    public FcmNotificationSender(String userFcmToken, String title, String body, Context mcontext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mcontext = mcontext;
        this.mActivity = mActivity;
    }

    public  void sendNotifications(){
        requestQueue= Volley.newRequestQueue(mActivity);
        JSONObject mainOBJ=new JSONObject();
        try{
            mainOBJ.put("to", userFcmToken);
            JSONObject notiObject=new JSONObject();
            notiObject.put("title",title);
            notiObject.put("body", body);
            notiObject.put("icon", R.drawable.icon); // enter icon that exists in drawable only
            mainOBJ.put("notification", notiObject);
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, postUrl, mainOBJ, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // code run is got response
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //code run is got error
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError{
                    Map<String,String> header=new HashMap<>();
                    header.put("content-type","application/Json");
                    header.put("authorization", "key" + fcmServerKey);
                    return header;
                }
            };
            requestQueue.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
