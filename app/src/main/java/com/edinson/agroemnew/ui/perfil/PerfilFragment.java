package com.edinson.agroemnew.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.edinson.agroemnew.databinding.FragmentPerfilBinding;
import com.edinson.agroemnew.modelApi.UserDetails;


public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel perfilViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        setupObservers();
        setupListeners();

        perfilViewModel.updateUserInterface();
    }

    private void setupObservers() {
        perfilViewModel.getUserData().observe(getViewLifecycleOwner(), this::updateUI);
        perfilViewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::showError);
    }

    private void updateUI(UserDetails.Sub userData) {
        binding.tvNombre.setText(userData.getNombre() + " " + userData.getApellido());
        binding.tvEmail.setText(userData.getEmail());
        binding.tvTelefono.setText(userData.getTelefono());
    }

    private void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void setupListeners() {
        binding.btnEditar.setOnClickListener(v -> {
            // Implementar la navegación a la pantalla de edición de usuario
            // Por ejemplo:
            // Navigation.findNavController(v).navigate(R.id.action_perfilFragment_to_usuarioEditarFragment);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        perfilViewModel.updateUserInterface();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}