package com.edinson.agroemnew.Usuario;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.usuario.UserDetails;
import com.edinson.agroemnew.modelApi.usuario.UserUpdate;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioEditar extends AppCompatActivity {
    private EditText edtNombre, edtApellido, edtEmail, edtNumIdentificacion, edtTelefono, edtNacimiento, edtCaracterizacion;
    private Button btnguardardatos, btnEditEmail;
    private ApiService apiService;
    private boolean isEmailEditable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usuario_editar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initializeViews();
        setupListeners();
        showUserIdToast(); // Verifica si el ID del usuario está guardado
    }

    private void initializeViews() {
        edtNombre = findViewById(R.id.edtNombre);
        edtApellido = findViewById(R.id.edtApellido);
        edtEmail = findViewById(R.id.edtEmail);
        edtNumIdentificacion = findViewById(R.id.edtNumIdentificacion);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtNacimiento = findViewById(R.id.edtNacimiento);
        btnguardardatos = findViewById(R.id.btnguardardatos);
        btnEditEmail = findViewById(R.id.btnEditarEmail);

        edtNacimiento.setOnClickListener(v -> showDatePickerDialog());
    }

    private void setupListeners() {
        btnEditEmail.setOnClickListener(v -> toggleEmailEditable());
        btnguardardatos.setOnClickListener(v -> actualizarDatosUsuario());
    }

    private void toggleEmailEditable() {
        isEmailEditable = !isEmailEditable;
        edtEmail.setEnabled(isEmailEditable);
        btnEditEmail.setText(isEmailEditable ? "Ocultar" : "Editar");
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatosUsuario();

    }


    private void showUserIdToast() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String userId = sharedPreferences.getString("UserId", null);

        if (userId != null) {
            //Toast.makeText(this, "ID del usuario guardado: " + userId, Toast.LENGTH_SHORT).show();
            Log.d("UsuarioEditar", "ID del usuario recuperado: " + userId);
        } else {
            Toast.makeText(this, "ID del usuario no encontrado", Toast.LENGTH_SHORT).show();
            Log.e("UsuarioEditar", "ID del usuario no encontrado en SharedPreferences");
        }
    }

    private void cargarDatosUsuario() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);
        if (token == null) {
            Log.e("EditarPerfilUsuario", "Token no encontrado en SharedPreferences");
            Toast.makeText(this, "Token no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }
        apiService = new ApiLogin().getRetrofitInstance().create(ApiService.class);
        Call<UserDetails> call = apiService.getUserDetails("Bearer " + token);
        Log.d("EditarPerfilUsuario", "Llamada a la API con token: " + token);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                Log.d("EditarPerfilUsuario", "Código de respuesta: " + response.code());
                if (response.isSuccessful()) {
                    UserDetails userDetails = response.body();
                    if (userDetails != null) {
                        Log.d("EditarPerfilUsuario", "Respuesta completa: " + new Gson().toJson(userDetails));
                        UserDetails.Sub sub = userDetails.getSub();
                        if (sub != null) {
                            Log.d("EditarPerfilUsuario", "ID del usuario en respuesta: " + sub.get_id());
                            runOnUiThread(() -> mostrarDatosEnUI(sub));
                        } else {
                            Log.e("EditarPerfilUsuario", "Objeto 'sub' es null en la respuesta");
                        }
                    } else {
                        Log.e("EditarPerfilUsuario", "UserDetails es null en la respuesta");
                    }
                } else {
                    handleApiError(response);
                }
            }
            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Log.e("EditarPerfilUsuario", "Error de conexión: " + t.getMessage(), t);
                Toast.makeText(UsuarioEditar.this, "Falló la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void mostrarDatosEnUI(UserDetails.Sub sub) {
        if (edtNombre != null && sub.getNombre() != null) edtNombre.setText(sub.getNombre());
        if (edtApellido != null && sub.getApellido() != null) edtApellido.setText(sub.getApellido());
        if (edtEmail != null && sub.getEmail() != null) edtEmail.setText(sub.getEmail());
        if (edtNumIdentificacion != null && sub.getNumIdentificacion() != null) edtNumIdentificacion.setText(sub.getNumIdentificacion());
        if (edtTelefono != null && sub.getTelefono() != null) edtTelefono.setText(sub.getTelefono());
        if (edtNacimiento != null && sub.getFechaNacimiento() != null) {
            // Extrae la fecha en el formato deseado
            String fechaNacimiento = sub.getFechaNacimiento().substring(0, 10);
            edtNacimiento.setText(fechaNacimiento);
        }
        if (edtCaracterizacion != null && sub.getCaracterizacion() != null) edtCaracterizacion.setText(sub.getCaracterizacion());
    }

    private void actualizarDatosUsuario() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);
        String userId = sharedPreferences.getString("UserId", null);

        if (token == null || userId == null) {
            Toast.makeText(this, "Información de usuario no encontrada", Toast.LENGTH_SHORT).show();
            Log.e("ActualizarDatosUsuario", "Token o ID de usuario no encontrado");
            return;
        }

        UserUpdate userUpdate = createUserUpdateObject();

        apiService = new ApiLogin().getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.updateUserProfile(userId, "Bearer " + token, userUpdate);
        Log.d("ActualizarDatosUsuario", "Llamada a la API para actualizar datos con ID: " + userId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("ActualizarDatosUsuario", "Código de respuesta: " + response.code());
                if (response.isSuccessful()) {
                    Log.d("ActualizarDatosUsuario", "Actualización exitosa");
                    Toast.makeText(UsuarioEditar.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();

                    obtenerDatosActualizados();
                } else {
                    Log.e("ActualizarDatosUsuario", "Error en la actualización: " + response.message());
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ActualizarDatosUsuario", "Error de conexión: " + t.getMessage(), t);
                Toast.makeText(UsuarioEditar.this, "Falló la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerDatosActualizados() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);

        if (token == null) {
            Toast.makeText(this, "Token no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = new ApiLogin().getRetrofitInstance().create(ApiService.class);
        Call<UserDetails> call = apiService.getUserDetails("Bearer " + token);

        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserDetails.Sub sub = response.body().getSub();
                    if (sub != null) {

                        setResult(Activity.RESULT_OK, new Intent().putExtra("DATOS_ACTUALIZADOS", true));
                        finish();
                    } else {
                        Toast.makeText(UsuarioEditar.this, "Datos de usuario no disponibles", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UsuarioEditar.this, "Error al obtener datos actualizados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Toast.makeText(UsuarioEditar.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private UserUpdate createUserUpdateObject() {
        UserUpdate userUpdate = new UserUpdate();
        userUpdate.setNombre(edtNombre.getText().toString());
        userUpdate.setApellido(edtApellido.getText().toString());
        userUpdate.setNumIdentificacion(edtNumIdentificacion.getText().toString());
        userUpdate.setTelefono(edtTelefono.getText().toString());

        // Asegúrate de que el formato de la fecha es el correcto
        String inputDate = edtNacimiento.getText().toString();
        try {
            LocalDate date = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String isoDate = date.format(DateTimeFormatter.ISO_DATE);
            userUpdate.setFechaNacimiento(isoDate);
        } catch (Exception e) {
            Log.e("ActualizarDatosUsuario", "Error al formatear la fecha: " + e.getMessage());
            // Manejar el error, tal vez mostrar un Toast al usuario
        }

        if (isEmailEditable) {
            userUpdate.setEmail(edtEmail.getText().toString());
        }

        Log.d("ActualizarDatosUsuario", "Datos a enviar: " + new Gson().toJson(userUpdate));
        return userUpdate;
    }

    private void showDatePickerDialog() {
        LocalDate currentDate = LocalDate.now();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    LocalDate selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    edtNacimiento.setText(selectedDate.format(formatter));
                },
                currentDate.getYear(),
                currentDate.getMonthValue() - 1,
                currentDate.getDayOfMonth()
        );
        datePickerDialog.show();
    }


    private void handleApiError(Response<?> response) {
        Log.e("ApiError", "Código de respuesta: " + response.code() + ", Mensaje: " + response.message());
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Sin cuerpo de error";
            Log.e("ApiError", "Cuerpo del error: " + errorBody);
            Toast.makeText(this, "Error: " + errorBody, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("ApiError", "Error al leer el cuerpo del error", e);
        }
    }
}