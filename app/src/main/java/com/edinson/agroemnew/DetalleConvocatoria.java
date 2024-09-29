package com.edinson.agroemnew;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.notificaciones.Convocatoria;
import com.edinson.agroemnew.modelApi.notificaciones.Plantilla;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleConvocatoria extends AppCompatActivity {

    private TextView tvTituloConvocatoria;
    private TextView tvDescripcionConvocatoria;
    private TextView tvFechaInicio;
    private TextView tvFechaCierre;
    private TextView tvEstadoConvocatoria;
    private LinearLayout llTemplates, llFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_convocatoria);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Inicializar vistas
        tvTituloConvocatoria = findViewById(R.id.tvTituloConvocatoria);
        tvDescripcionConvocatoria = findViewById(R.id.tvDescripcionConvocatoria);
        tvFechaInicio = findViewById(R.id.tvFechaInicio);
        tvFechaCierre = findViewById(R.id.tvFechaCierre);
        tvEstadoConvocatoria = findViewById(R.id.tvEstadoConvocatoria);
        llTemplates = findViewById(R.id.llTemplates);
        llFiles = findViewById(R.id.llFiles);

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

                    // Mostrar detalles de la convocatoria en la UI
                    tvTituloConvocatoria.setText(convocatoria.getTitle());
                    tvDescripcionConvocatoria.setText(convocatoria.getDescripcion());

                    // Formatear fechas
                    String fechaInicioFormato = formatearFecha(convocatoria.getFechaInicio(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd/MM/yyyy");
                    String fechaCierreFormato = formatearFecha(convocatoria.getFechaCierre(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd/MM/yyyy");

                    tvFechaInicio.setText("Fecha de Inicio: " + fechaInicioFormato);
                    tvFechaCierre.setText("Fecha de Cierre: " + fechaCierreFormato);
                    tvEstadoConvocatoria.setText("Estado: " + convocatoria.getEstado());

                    // Mostrar plantilla
                    mostrarTemplate(convocatoria.getTemplate());

                    // Mostrar archivos si existen
                    mostrarFiles(convocatoria.getFiles());
                } else {
                    Log.e("DetalleConvocatoria", "Error en la respuesta: " + response.code() + " - " + response.message());
                    Toast.makeText(DetalleConvocatoria.this, "Error al obtener los detalles", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<Convocatoria> call, Throwable t) {
                Log.e("DetalleConvocatoria", "Error de conexión: ", t);
                Toast.makeText(DetalleConvocatoria.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }




    // Update mostrarTemplate to handle a single Template object
    private void mostrarTemplate(List<String> templates) {
        llTemplates.removeAllViews(); // Limpia las vistas existentes

        if (templates != null && !templates.isEmpty()) {
            for (String templateJson : templates) {
                try {

                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<TemplateItem>>(){}.getType();
                    List<TemplateItem> templateItems = gson.fromJson(templateJson, listType);

                    // Crear un TextView para mostrar los items del template
                    TextView textView = new TextView(this);
                    StringBuilder templateContent = new StringBuilder("");
                    for (TemplateItem item : templateItems) {
                        templateContent.append("- ").append(item.getTitulo()).append("\n");
                    }
                    textView.setText(templateContent.toString());
                    llTemplates.addView(textView);
                } catch (Exception e) {
                    Log.e("DetalleConvocatoria", "Error al parsear template: " + e.getMessage());
                    TextView errorView = new TextView(this);
                    errorView.setText("Error al mostrar el template");
                    llTemplates.addView(errorView);
                }
            }
        } else {
            // Muestra un mensaje si no hay templates disponibles
            TextView textView = new TextView(this);
            textView.setText("No hay templates disponibles");
            llTemplates.addView(textView);
        }
    }

    // Clase interna para representar un item del template
    private static class TemplateItem {
        private String titulo;

        public String getTitulo() {
            return titulo;
        }
    }




    // Mostrar los archivos adjuntos
    private void mostrarFiles(List<String> files) {
        llFiles.removeAllViews();
        if (files != null && !files.isEmpty()) {
            for (String fileUrl : files) {
                // Crear un ImageView para mostrar el ícono de descarga
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.file_download);

                // Configurar el listener para descargar el archivo al hacer clic
                imageView.setOnClickListener(v -> descargarArchivo(fileUrl));

                // Agregar el ícono a la vista
                llFiles.addView(imageView);
            }
        } else {
            // Mostrar mensaje si no hay archivos
            TextView textView = new TextView(this);
            textView.setText("No hay archivos disponibles");
            llFiles.addView(textView);
        }
    }

    // Método para descargar archivos
    private void descargarArchivo(String url) {
        Toast.makeText(DetalleConvocatoria.this, "Descargando archivo...", Toast.LENGTH_SHORT).show();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Descargando archivo");
        request.setDescription("Descargando el archivo desde la convocatoria");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url.substring(url.lastIndexOf('/') + 1));

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }

    // Método para cambiar el formato de las fechas
    private String formatearFecha(String fechaOriginal, String formatoEntrada, String formatoSalida) {
        SimpleDateFormat formatoEntradaDate = new SimpleDateFormat(formatoEntrada, Locale.getDefault());
        SimpleDateFormat formatoSalidaDate = new SimpleDateFormat(formatoSalida, Locale.getDefault());
        try {
            Date date = formatoEntradaDate.parse(fechaOriginal);
            return formatoSalidaDate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return fechaOriginal;
        }
    }
}
