package com.edinson.agroemnew;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.edinson.agroemnew.proyecto.NtfProyectos;
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
            Log.d(TAG, "Mensaje de datos: " + message.getData());
            mostrarNotificacion(message.getData());
        } else if (message.getNotification() != null) {
            Log.d(TAG, "Mensaje de notificación: " + message.getNotification().getBody());
            mostrarNotificacion(message.getNotification().getBody());
        }
    }

    private void mostrarNotificacion(Map<String, String> data) {
        String title = data.get("title");
        String body = data.get("body");

        Intent intent = new Intent(this, NtfProyectos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.megafono)
                .setContentTitle(title != null ? title : "Nueva Notificación")
                .setContentText(body != null ? body : "Tienes una nueva notificación")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }

    private void mostrarNotificacion(String mensaje) {
        Intent intent = new Intent(this, NtfProyectos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.megafono)
                .setContentTitle("Nueva Notificación")
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }
}
