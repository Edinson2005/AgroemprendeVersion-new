package com.edinson.agroemnew;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyfirebaseMsgService";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "onNewToken: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        super.onMessageReceived(message);

        if (message.getData().size() > 0) {
            Log.d(TAG, "Mensaje recibido" + message.getData());

            String titulo = message.getData().get("title");
            String cuerpo = message.getData().get("body");

            mostrarNotificacion(titulo, cuerpo);
        }
    }

    private void mostrarNotificacion(String titulo, String cuerpo) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CANAL_ID")
                .setSmallIcon(R.drawable.megafono)  // Icono de la notificación
                .setContentTitle(titulo)
                .setContentText(cuerpo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true); // Se cancela al hacer clic en la notificación

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }


}
