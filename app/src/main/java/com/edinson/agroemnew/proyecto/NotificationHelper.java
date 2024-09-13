package com.edinson.agroemnew.proyecto;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.edinson.agroemnew.R;

public class NotificationHelper {

    public static final String CHANNEL_ID = "agroem_channel";
    private static final CharSequence CHANNEL_NAME = "Canal de Notificaciones de Agroem";

    // Método para crear el canal de notificaciones (necesario para Android 8 y superior)
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "Canal para notificaciones de proyectos";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            // Crea el canal de notificaciones
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);

            // Registra el canal con el sistema
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    // Método para enviar una notificación
    public static void sendNotification(Context context, String title, String content) {
        // Crea un Intent para abrir una actividad cuando se haga clic en la notificación
        Intent intent = new Intent(context, NtfProyectos.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Crea un PendingIntent para pasar al NotificationCompat.Builder
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Construye la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.img_9)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_9))
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content)) // Añade un estilo BigText
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.GREEN)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true); // La notificación se cancelará automáticamente al hacer clic

        // Muestra la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}
