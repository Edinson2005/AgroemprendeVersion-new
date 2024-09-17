package com.edinson.agroemnew;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.edinson.agroemnew.Usuario.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";
    private static final String CHANNEL_ID = MyApplication.CHANNEL_ID;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Nuevo token FCM recibido: " + token);
        // Aquí puedes enviar el token a tu servidor si es necesario
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d(TAG, "Mensaje recibido de FCM");

        // Enviar un broadcast local para fines de depuración
        Intent intent = new Intent("NOTIFICACION_RECIBIDA");
        intent.putExtra("mensaje", "Notificación recibida en " + System.currentTimeMillis());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        if (message.getData().size() > 0) {
            Log.d(TAG, "Mensaje de datos recibido: " + message.getData());
            handleDataMessage(message.getData());
        }

        if (message.getNotification() != null) {
            Log.d(TAG, "Mensaje de notificación recibido: " + message.getNotification().getBody());
            handleNotificationMessage(message.getNotification());
        }
    }

    private void handleDataMessage(Map<String, String> data) {
        String titulo = data.get("title");
        String cuerpo = data.get("body");
        Log.d(TAG, "Manejando mensaje de datos. Título: " + titulo + ", Cuerpo: " + cuerpo);
        mostrarNotificacion(titulo, cuerpo);
    }

    private void handleNotificationMessage(RemoteMessage.Notification notification) {
        String titulo = notification.getTitle();
        String cuerpo = notification.getBody();
        Log.d(TAG, "Manejando mensaje de notificación. Título: " + titulo + ", Cuerpo: " + cuerpo);
        mostrarNotificacion(titulo, cuerpo);
    }

    private void mostrarNotificacion(String titulo, String cuerpo) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.megafono)
                .setContentTitle(titulo)
                .setContentText(cuerpo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        try {
            notificationManager.notify(1, builder.build());
            Log.d(TAG, "Notificación mostrada con éxito");
        } catch (SecurityException e) {
            Log.e(TAG, "No se pudo mostrar la notificación: " + e.getMessage());
        }
    }
}