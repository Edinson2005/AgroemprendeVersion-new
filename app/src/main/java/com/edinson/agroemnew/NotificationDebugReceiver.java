package com.edinson.agroemnew;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationDebugReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationDebugReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("NOTIFICACION_RECIBIDA".equals(intent.getAction())) {
            String mensaje = intent.getStringExtra("mensaje");
            Log.d(TAG, "Notificación recibida: " + mensaje);

            // Registrar en un archivo de log
            registrarEnArchivo(context, mensaje);
        }
    }

    private void registrarEnArchivo(Context context, String mensaje) {
        File logFile = new File(context.getExternalFilesDir(null), "notificacion_log.txt");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = dateFormat.format(new Date());

        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.append(timestamp).append(": ").append(mensaje).append("\n");
        } catch (IOException e) {
            Log.e(TAG, "Error al escribir en el archivo de log", e);
        }
    }
}