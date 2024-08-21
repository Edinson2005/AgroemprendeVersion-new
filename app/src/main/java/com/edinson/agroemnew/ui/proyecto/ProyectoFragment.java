package com.edinson.agroemnew.ui.proyecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.InformacionProyecto;
import com.edinson.agroemnew.R;
import com.edinson.agroemnew.databinding.FragmentProyectoBinding;
import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.Proyecto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProyectoFragment extends Fragment {
    private RecyclerView recyclerViewProyectos;
    private ProyectoAdapter proyectoAdapter;
    private List<Proyecto> proyectoList = new ArrayList<>();
    private ApiService apiService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proyecto, container, false);

        recyclerViewProyectos = view.findViewById(R.id.recyclerViewProyectos);
        recyclerViewProyectos.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa tu ApiService aquí usando ApiLogin
        apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);

        // Inicializa el adaptador con el OnItemClickListener
        proyectoAdapter = new ProyectoAdapter(getContext(), proyectoList, proyectoId -> {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyApp", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("SelectID", proyectoId);
            editor.apply();

            Intent intent = new Intent(getContext(), InformacionProyecto.class);
            startActivity(intent);
        });

        recyclerViewProyectos.setAdapter(proyectoAdapter);

        obtenerProyectos();

        return view;
    }

    private void obtenerProyectos() {
        // Recupera el token de SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", "");

        if (token.isEmpty()) {
            Toast.makeText(getContext(), "Token de autenticación no encontrado", Toast.LENGTH_LONG).show();
            return;
        }

        // Agrega "Bearer " al token
        String authToken = "Bearer " + token;

        apiService.getProyecto(authToken).enqueue(new Callback<List<Proyecto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Proyecto>> call, @NonNull Response<List<Proyecto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    proyectoList.clear();
                    proyectoList.addAll(response.body());
                    proyectoAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Error en la respuesta de la API: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Proyecto>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error al conectar con la API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}