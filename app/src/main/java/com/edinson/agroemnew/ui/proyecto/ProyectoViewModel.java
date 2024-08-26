package com.edinson.agroemnew.ui.proyecto;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.edinson.agroemnew.modelApi.Project;

import java.util.List;

public class ProyectoViewModel extends ViewModel {

    private final MutableLiveData<List<Project>> pruebaLiveData;
    private final MutableLiveData<String> errorMessage;

    public ProyectoViewModel() {
        pruebaLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<List<Project>> getPrueba() {
        return pruebaLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setPruebas(List<Project> projects) {
        pruebaLiveData.setValue(projects);
    }

    public void setErrorMessage(String message) {
        errorMessage.setValue(message);
    }
}