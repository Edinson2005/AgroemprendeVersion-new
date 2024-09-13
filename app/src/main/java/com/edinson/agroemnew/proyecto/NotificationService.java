package com.edinson.agroemnew.proyecto;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.edinson.agroemnew.R;

import static com.edinson.agroemnew.proyecto.NotificationHelper.CHANNEL_ID;

public class NotificationService extends Service {
    private static final String TAG = "NotificationService";
    private int notificationId = 0; // ID de notificación único

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        // Inicia el servicio en primer plano con una notificación
        startForeground(1, createNotification("Servicio en primer plano", "El servicio está corriendo"));

        // Programa una tarea que se ejecuta cada segundo
        scheduleWorker();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification createNotification(String title, String text) {
        // Crea la notificación con un ícono, título y texto
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.img_9)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img_9))
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH); // Define la prioridad de la notificación

        return builder.build();
    }

    private void scheduleWorker() {
        // Crea un ScheduledExecutorService para ejecutar una tarea periódica
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Programa la tarea para  cada segundo
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Ejecutando tarea periódica");

                // Crea una notificación única para cada tarea
                String title = "Título de la notificación " + notificationId;
                String text = "Texto de la notificación " + notificationId;
                Notification notification = createNotification(title, text);

                // Usa NotificationManagerCompat para emitir la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationService.this);
                notificationManager.notify(notificationId, notification);

                // Incrementa el ID de notificación para la próxima tarea
                notificationId++;
            }
        }, 0, 1, TimeUnit.SECONDS); // Ejecuta cada segundo
    }
}