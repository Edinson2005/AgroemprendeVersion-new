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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.modelApi.Project;
import com.edinson.agroemnew.proyecto.InformacionProyecto;
import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProyectoFragment extends Fragment {
    private RecyclerView recyclerViewProyectos;
    private ProyectoAdapter proyectoAdapter;
    private List<Project> projectList = new ArrayList<>();
    private ApiService apiService;
    private ProyectoViewModel proyectoViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proyecto, container, false);

        recyclerViewProyectos = view.findViewById(R.id.recyclerViewProyectos);

        // Configura el GridLayoutManager con 2 columnas
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewProyectos.setLayoutManager(gridLayoutManager);

        // Inicializa el ApiService
        apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);

        // Inicializa el ViewModel
        proyectoViewModel = new ProyectoViewModel();

        // Inicializa el adaptador con el OnItemClickListener
        proyectoAdapter = new ProyectoAdapter(getContext(), projectList, pruebaId -> {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyApp", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("SelectID", pruebaId); // Cambiado de proyectoId a pruebaId
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

        apiService.getPrueba(authToken).enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(@NonNull Call<List<Project>> call, @NonNull Response<List<Project>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    projectList.clear();
                    projectList.addAll(response.body());
                    proyectoViewModel.setPruebas(projectList); // Actualiza el ViewModel con la lista ordenada
                    proyectoAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Error en la respuesta de la API: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Project>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error al conectar con la API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}