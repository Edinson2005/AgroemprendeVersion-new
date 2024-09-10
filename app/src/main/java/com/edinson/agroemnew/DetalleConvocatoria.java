package com.edinson.agroemnew;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.notificaciones.Convocatoria;
import com.edinson.agroemnew.modelApi.notificaciones.Template;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleConvocatoria extends AppCompatActivity {

    private TextView tvTitulo, tvDescripcion, tvFechaInicio, tvFechaCierre, tvEstado;
    private LinearLayout llTemplates;
    private String convocatoriaID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_convocatoria);

        //Inicializar vistas
        tvTitulo =  findViewById(R.id.tvTituloConvocatoria);
        tvDescripcion = findViewById(R.id.tvDescripcionConvocatoria);
        tvFechaInicio = findViewById(R.id.tvFechaInicio);
        tvFechaCierre = findViewById(R.id.tvFechaCierre);
        tvEstado = findViewById(R.id.tvEstadoConvocatoria);
        llTemplates = findViewById(R.id.llTemplates);

        //obtner el id de la convocatoria del Intent
        convocatoriaID = getIntent().getStringExtra("convocatoria_id");
        if(convocatoriaID != null){
            obtnerDetalles(convocatoriaID);
        } else {
            Toast.makeText(this, "Convocatoria no encontrada", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void obtnerDetalles(String id){
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);

        if(token != null){
            ApiService apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);
            //hacer llamado a l api
            Call<Convocatoria> call = apiService.getConvocatoria("Bearer " + token, id);
            call.enqueue(new Callback<Convocatoria>() {
                @Override
                public void onResponse(Call<Convocatoria> call, Response<Convocatoria> response) {
                    if(response.isSuccessful() && response.body() != null){
                        mostrarDetalles(response.body());
                    } else{
                        Toast.makeText(DetalleConvocatoria.this, "Error al obtener los detalles", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Convocatoria> call, Throwable t) {
                    Toast.makeText(DetalleConvocatoria.this, "Fallo en la conexion", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Token no disponible", Toast.LENGTH_SHORT).show();
        }
    }
    private void mostrarDetalles(Convocatoria convocatoria){
        tvTitulo.setText(convocatoria.getTitle());
        tvDescripcion.setText(convocatoria.getDescripcion());
        tvFechaInicio.setText("fecha de inicio: " + convocatoria.getFechaInicio());
        tvFechaCierre.setText("fecha de cierre: " + convocatoria.getFechaCierre());
        tvEstado.setText("Estado: "+ convocatoria.getEstado());

        //mostrar los templantes
        List<Template> templates = convocatoria.getTemplate();
        if(templates != null && !templates.isEmpty()){
            for (Template template: templates){
                TextView tvTemplate = new TextView(this);
                tvTemplate.setText("- " + template.getTitulo());
                tvTemplate.setTextSize(14f);
                llTemplates.addView(tvTemplate);
            }
        }else{
            TextView tvNoTemplates = new TextView(this);
            tvNoTemplates.setText("No hay templates disponibles");
            llTemplates.addView(tvNoTemplates);
        }


    }

}