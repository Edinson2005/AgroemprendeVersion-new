package com.edinson.agroemnew;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.Forgot;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecuperarPassword extends AppCompatActivity {

    private EditText etCorreo;
    private Button btnEnviar;
    private ApiService apiService;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuperar_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
         etCorreo = findViewById(R.id.etCorreoElectronicorecuperacion);
        btnEnviar = findViewById(R.id.btnenviarrecuperacion);
        progressBar= findViewById(R.id.progressBar);

        apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etCorreo.getText().toString().trim();
                if (!TextUtils.isEmpty(email)) {
                    sendForgotPasswordRequest(email);
                }else {
                    Toast.makeText(RecuperarPassword.this, "Por favor, ingresa tu correo electrónico", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendForgotPasswordRequest( String email){
        showLoading(true);
        Forgot forgot = new Forgot(email);
        Call<Forgot> call = apiService.forgotPassword(forgot);

        call.enqueue(new Callback<Forgot>() {
            @Override
            public void onResponse(Call<Forgot> call, Response<Forgot> response) {
                showLoading(false);
                if(response.isSuccessful()) {
                    Toast.makeText(RecuperarPassword.this,"Se ha enviado un correo de recuperacion ", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(RecuperarPassword.this,"Error al enviar el correo de recuperacion ", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Forgot> call, Throwable t) {
                showLoading(false);
                Toast.makeText(RecuperarPassword.this,"Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnEnviar.setEnabled(false);

        }else{
            progressBar.setVisibility(View.GONE);
            btnEnviar.setEnabled(true);
        }
    }
}