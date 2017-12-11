package com.football.freekick.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.orhanobut.logger.Logger;

/**
 * Created by ly on 2017/11/29.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FREEKICK";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseMessaging.getInstance().subscribeToTopic("mytopic");
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.d("refreshedToken" + refreshedToken);
        Log.d(TAG, "Refreshed token: " + refreshedToken);
//        sendRegistrationToServer(refreshedToken);
    }

//    private void sendRegistrationToServer(String token) {
//        // TODO: Implement this method to send token to your app server.
//        Firebase.getInstance(this).logEvent("user_id", token);
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("mytopic");
    }
}
