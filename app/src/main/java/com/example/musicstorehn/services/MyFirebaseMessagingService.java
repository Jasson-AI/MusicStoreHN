package com.example.musicstorehn.services;

// File: app/src/main/java/com/uth/musicstorehn/services/MyFirebaseMessagingService.java

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.example.musicstorehn.R;
import com.example.musicstorehn.activities.MainActivity;
import com.example.musicstorehn.network.RetrofitClient;
import com.example.musicstorehn.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static final String CHANNEL_ID = "music_store_notifications";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d(TAG, "From: " + message.getFrom());

        if (message.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + message.getData());
        }

        if (message.getNotification() != null) {
            String title = message.getNotification().getTitle();
            String body = message.getNotification().getBody();

            Log.d(TAG, "Message Notification Title: " + title);
            Log.d(TAG, "Message Notification Body: " + body);

            sendNotification(title, body);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
        sendTokenToServer(token);
    }

    private void sendTokenToServer(String token) {
        SessionManager session = new SessionManager(this);

        if (session.isLoggedIn()) {
            RetrofitClient.getApiService()
                    .updateFcmToken(session.getAuthToken(), token)
                    .enqueue(new Callback<com.example.musicstorehn.models.Response<String>>() {
                        @Override
                        public void onResponse(Call<com.example.musicstorehn.models.Response<String>> call, Response<com.example.musicstorehn.models.Response<String>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Log.d(TAG, "Token actualizado en servidor");
                            }
                        }

                        @Override
                        public void onFailure(Call<com.example.musicstorehn.models.Response<String>> call, Throwable t) {
                            Log.e(TAG, "Error al actualizar token: " + t.getMessage());
                        }
                    });
        }
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Store Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notificaciones de nuevos audios y videos");
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}
