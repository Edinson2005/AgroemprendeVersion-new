package com.edinson.agroemnew.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Integer> projectCountLiveData;
    private final MutableLiveData<String> errorMessage;

    public HomeViewModel() {
        projectCountLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<Integer> getProjectCount() {
        return projectCountLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setProjectCount(int count) {
        projectCountLiveData.setValue(count);
    }

    public void setErrorMessage(String message) {
        errorMessage.setValue(message);
    }
}