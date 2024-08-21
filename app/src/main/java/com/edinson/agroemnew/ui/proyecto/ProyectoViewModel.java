package com.edinson.agroemnew.ui.proyecto;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.edinson.agroemnew.modelApi.Proyecto;

import java.util.List;

public class ProyectoViewModel extends ViewModel {

    private final MutableLiveData<List<Proyecto>> proyectosLiveData;
    private final MutableLiveData<String> errorMessage;

    public ProyectoViewModel() {
        proyectosLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<List<Proyecto>> getProyectos() {
        return proyectosLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setProyectos(List<Proyecto> proyectos) {
        proyectosLiveData.setValue(proyectos);
    }

    public void setErrorMessage(String message) {
        errorMessage.setValue(message);
    }
}