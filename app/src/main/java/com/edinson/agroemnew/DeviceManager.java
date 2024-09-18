package com.edinson.agroemnew;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeviceManager {

    private static final String TAG = "DeviceManager"; // Para log

    public static void postRegistrarDispositivoEnServidor(String token, Context context) {
        // Instanciar RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "url_servidor";  // Cambia esto por la URL real de tu servidor

        // Solicitud
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resoObj = new JSONObject(response);
                            String code = resoObj.getString("code");
                            String message = resoObj.getString("message");
                            Integer id = resoObj.getInt("id");

                            if ("OK".equals(code)) {
                                if (id != 0) {
                                    context.getSharedPreferences("SP_FILE", 0)
                                            .edit()
                                            .putInt("ID", id)
                                            .apply(); // Mejor usar apply() que commit()
                                    Log.d(TAG, "ID registrado correctamente: " + id);
                                }
                            } else {
                                Log.w(TAG, "Respuesta del servidor: " + message);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error en la respuesta JSON", e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error registrando token: " + error.toString());
                Toast.makeText(context, "Error registrando token: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Obtener DEVICEID desde SharedPreferences
                String deviceId = context.getSharedPreferences("SP_FILE", 0)
                        .getString("DEVICEID", null);

                // Agregar parámetros
                params.put("DEVICEID", deviceId != null ? deviceId : token); // Usa el token si DEVICEID es nulo
                int storedId = context.getSharedPreferences("SP_FILE", 0)
                        .getInt("ID", 0);

                if (storedId != 0) {
                    params.put("ID", String.valueOf(storedId));
                }

                return params;
            }
        };

        // Agregar la solicitud a la cola
        queue.add(stringRequest);
    }
}
