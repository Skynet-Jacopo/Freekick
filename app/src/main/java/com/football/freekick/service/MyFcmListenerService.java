package com.football.freekick.service;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by ly on 2017/11/29.
 */

public class MyFcmListenerService extends FirebaseMessagingService {
    private static final String TAG = "FREEKICK";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String from = remoteMessage.getFrom();
        Map data = remoteMessage.getData();
        Log.d(TAG, "onMessageReceived: "+from);
        Log.d(TAG, "onMessageReceived: "+data.toString());

        data = remoteMessage.getData();
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String imageUrl = (String) data.get("image");
        String action = (String) data.get("action");
        Log.i(TAG, "onMessageReceived: title : "+title);
        Log.i(TAG, "onMessageReceived: message : "+message);
        Log.i(TAG, "onMessageReceived: imageUrl : "+imageUrl);
        Log.i(TAG, "onMessageReceived: action : "+action);

        if (imageUrl == null) {
            sendNotification(title,message,action);
        } else {
//            new BigPictureNotification(this,title,message,imageUrl,action);
        }

    }

    private void sendNotification(String title, String message, String action) {

    }

    @Override
    public void zzm(Intent intent) {
        super.zzm(intent);
    }
}