package com.github.proglottis.ladders;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by steventan on 17/09/15.
 */
public class MyGcmListenerService extends GcmListenerService {
    private static final String COLLAPSE_KEY = "collapse_key";
    private static final String GAME_CONFIRMATION = "game_confirmation";
    private static final String GAME_ID_KEY = "game_id";

    public static final int CONFIRMATION_REQUEST_CODE = 1000;
    public static final int NOTIFICATION_ID = 1001;
    public static final String MESSAGE_KEY = "message";
    private String TAG = MyGcmListenerService.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        Log.d(TAG, from);

        Log.d(TAG, data.toString());

        String collapse = data.getString(COLLAPSE_KEY, "");

        Log.d(TAG, "Collapse: " + collapse);

        if (collapse.equals(GAME_CONFIRMATION)) {
            String gameId = data.getString(GAME_ID_KEY);
            String message = data.getString(MESSAGE_KEY);

            Intent confirmationIntent = new Intent(this, GameActivity.class);
            confirmationIntent.putExtra(GameActivity.GAME_ID, gameId);

            PendingIntent pendingintent = PendingIntent.getActivity(this, CONFIRMATION_REQUEST_CODE,
                    confirmationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new Notification.Builder(this)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingintent)
                    .getNotification();

            NotificationManager manager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(gameId, NOTIFICATION_ID, notification);
            Log.d(TAG, "Created a notification");
        }
    }
}
