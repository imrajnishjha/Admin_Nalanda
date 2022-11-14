package com.wormos.nalandaadmin;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseNotificationSender {

    String userFcmToken;
    String title;
    String body;
    Context mContext;
    Activity mActivity;
    int number;
    String togo;
    String ownerKey;
    String chatKey;
    String eventType;

    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey ="AAAAVO4QACk:APA91bFITz1713nh_uoJ6m8bIWujeyCsx00WPTjL1wOTrLX4sP2l-VvaR6pb7ukE9HJweWG9sNdo7DX7AWatErv5m19qHAs4nOlwnjn078dt8Vqdp3PavG-ttwNqS_o6OOdkqNYV0Rs9";

    public MyFirebaseNotificationSender(String userFcmToken, String title, String body, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    public MyFirebaseNotificationSender(String userFcmToken, String title, String body, Context mContext, Activity mActivity, String togo, String ownerKey, String chatKey) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.togo = togo;
        this.ownerKey = ownerKey;
        this.chatKey = chatKey;
    }

    public MyFirebaseNotificationSender(String userFcmToken, String title, String body, Context mContext, Activity mActivity, String togo, String ownerKey, String chatKey, String eventType) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.togo = togo;
        this.ownerKey = ownerKey;
        this.chatKey = chatKey;
        this.eventType = eventType;
    }

    public void SendNotifications() {

        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            JSONObject notifyObject = new JSONObject();
            notifyObject.put("title", title);
            notifyObject.put("body", body); // enter icon that exists in drawable only
            notifyObject.put("number", number);

            mainObj.put("notification", notifyObject);

            JSONObject extraData = new JSONObject();
            extraData.put("activity",togo);
            extraData.put("ownerKey",ownerKey);
            extraData.put("chatKey",chatKey);
            extraData.put("eventType",eventType);
            mainObj.put("data",extraData);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;

                }
            };
            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
