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
    // Definir el ID del canal de notificación
    public static final String CHANNEL_ID = "agroem_channel";
    // Definir el nombre del canal de notificación
    private static final CharSequence CHANNEL_NAME = "Canal de Notificaciones de Agroem";

    // Crear el canal de notificación para Android O y versiones posteriores
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_NAME;
            String description = "Canal para notificaciones de proyectos";
            int importance = NotificationManager.IMPORTANCE_HIGH; // Cambiado a HIGH para notificaciones más visibles
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public static void sendNotification(Context context, String title, String content) {
        Intent intent = new Intent(context, NtfProyectos.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.img_9) // Asegúrate de tener este ícono en res/drawable
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_9)) // Ícono grande para mayor visibilidad
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content)) // Estilo para mostrar texto extendido
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Aumenta la prioridad de la notificación
                .setColor(Color.GREEN) // Color del texto de la notificación
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}
