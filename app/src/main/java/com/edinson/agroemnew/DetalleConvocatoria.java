package com.edinson.agroemnew;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.notificaciones.Convocatoria;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleConvocatoria extends AppCompatActivity {

    private TextView tvTituloConvocatoria;
    private TextView tvDescripcionConvocatoria;
    private TextView tvFechaInicio;
    private TextView tvFechaCierre;
    private TextView tvEstadoConvocatoria;
    private LinearLayout llTemplates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_convocatoria);

        // Inicializar vistas
        tvTituloConvocatoria = findViewById(R.id.tvTituloConvocatoria);
        tvDescripcionConvocatoria = findViewById(R.id.tvDescripcionConvocatoria);
        tvFechaInicio = findViewById(R.id.tvFechaInicio);
        tvFechaCierre = findViewById(R.id.tvFechaCierre);
        tvEstadoConvocatoria = findViewById(R.id.tvEstadoConvocatoria);
        llTemplates = findViewById(R.id.llTemplates);

        // Obtener datos del Intent
        String convocatoriaId = getIntent().getStringExtra("convocatoria_id");
        String token = getIntent().getStringExtra("authorization");

        // Registrar el ID de la convocatoria en el log
        Log.d("DetalleConvocatoria", "ID de Convocatoria recibido: " + convocatoriaId);

        // Llamar a la API para obtener los detalles de la convocatoria
        obtenerDetallesConvocatoria(convocatoriaId, token);
    }

    private void obtenerDetallesConvocatoria(String convocatoriaId, String token) {
        ApiService apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);
        Call<Convocatoria> call = apiService.getConvocatoria(token, convocatoriaId);

        call.enqueue(new Callback<Convocatoria>() {
            @Override
            public void onResponse(Call<Convocatoria> call, Response<Convocatoria> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Convocatoria convocatoria = response.body();
                    // Mostrar los detalles en la UI
                    tvTituloConvocatoria.setText(convocatoria.getTitle());
                    tvDescripcionConvocatoria.setText(convocatoria.getDescripcion());
                    tvFechaInicio.setText("Fecha de Inicio: " + convocatoria.getFechaInicio());
                    tvFechaCierre.setText("Fecha de Cierre: " + convocatoria.getFechaCierre());
                    tvEstadoConvocatoria.setText("Estado: " + convocatoria.getEstado());

                    // Mostrar los templates
                    mostrarTemplates(convocatoria.getTemplate());
                } else {
                    Log.e("DetalleConvocatoria", "Error en la respuesta: " + response.code() + " - " + response.message());
                    Toast.makeText(DetalleConvocatoria.this, "Error al obtener los detalles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Convocatoria> call, Throwable t) {
                Log.e("DetalleConvocatoria", "Error de conexión: ", t); // Registra el error completo
                Toast.makeText(DetalleConvocatoria.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarTemplates(List<String> templates) {
        llTemplates.removeAllViews();
        for (String template : templates) {
            TextView textView = new TextView(this);
            textView.setText(template);
            llTemplates.addView(textView);
        }
    }
}