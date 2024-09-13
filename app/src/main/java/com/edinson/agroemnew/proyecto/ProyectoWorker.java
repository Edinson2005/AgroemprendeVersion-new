package com.edinson.agroemnew.proyecto;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.notificaciones.ProyectoNot;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProyectoWorker extends Worker {

    private static final String TAG = "ProyectoWorker";
    private ApiService apiService;
    private String token;
    private static final String PREFS_NAME = "MyApp";
    private static final String PREFS_KEY_NOTIFIED_IDS = "NotifiedIds";

    public ProyectoWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);
    }

    @NonNull
    @Override
    public Result doWork() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        token = sharedPreferences.getString("UserToken", "");

        if (token.isEmpty()) {
            Log.e(TAG, "Token de autenticación no encontrado.");
            return Result.failure();
        }

        // Realiza la solicitud a la API
        apiService.getProyectos("Bearer " + token).enqueue(new Callback<List<ProyectoNot>>() {
            @Override
            public void onResponse(Call<List<ProyectoNot>> call, Response<List<ProyectoNot>> response) {
                if (response.isSuccessful()) {
                    List<ProyectoNot> proyectos = response.body();
                    if (proyectos != null && !proyectos.isEmpty()) {
                        // Recupera los IDs de notificaciones ya enviadas
                        Set<String> notifiedIds = sharedPreferences.getStringSet(PREFS_KEY_NOTIFIED_IDS, new HashSet<>());

                        for (ProyectoNot proyecto : proyectos) {
                            // Verifica si ya se ha enviado una notificación para este proyecto
                            if (!notifiedIds.contains(proyecto.getId())) {
                                NotificationHelper.sendNotification(
                                        getApplicationContext(),
                                        "Nuevo Proyecto",
                                        proyecto.getTitle() + ": " + proyecto.getBody()
                                );

                                // Añade el ID del proyecto a la lista de IDs notificados
                                notifiedIds.add(proyecto.getId());
                            }
                        }

                        // Guarda los IDs de notificaciones ya enviadas
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putStringSet(PREFS_KEY_NOTIFIED_IDS, notifiedIds);
                        editor.apply();
                    } else {
                        Log.e(TAG, "Lista de proyectos es nula o está vacía.");
                    }
                    // Retorna el resultado exitoso
                    Result.success();
                } else {
                    Log.e(TAG, "Error al obtener los proyectos: " + response.message());
                    // Retorna el resultado de fallo
                    Result.failure();
                }
            }

            @Override
            public void onFailure(Call<List<ProyectoNot>> call, Throwable t) {
                Log.e(TAG, "Error en la solicitud: " + t.getMessage());
                // Retorna el resultado de fallo
                Result.failure();
            }
        });

        // Retorna un resultado exitoso si no se usan callbacks
        return Result.retry(); // O Result.success() dependiendo de tu lógica
    }
}
