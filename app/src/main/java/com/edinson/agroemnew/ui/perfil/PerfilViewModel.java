        package com.edinson.agroemnew.ui.perfil;

        import android.app.Application;
        import android.content.SharedPreferences;
        import android.util.Log;

        import androidx.annotation.NonNull;
        import androidx.lifecycle.AndroidViewModel;
        import androidx.lifecycle.LiveData;
        import androidx.lifecycle.MutableLiveData;

        import com.edinson.agroemnew.modelApi.ApiLogin;
        import com.edinson.agroemnew.modelApi.ApiService;
        import com.edinson.agroemnew.modelApi.UserDetails;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

        public class PerfilViewModel extends AndroidViewModel {
            private final MutableLiveData<UserDetails.Sub> _userData = new MutableLiveData<>();
            private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
            private final SharedPreferences sharedPreferences;
            private static final long CACHE_DURATION = 5 * 60 * 1000; // 5 minutos


            public PerfilViewModel(@NonNull Application application) {
                super(application);
                sharedPreferences = application.getSharedPreferences("MyApp", Application.MODE_PRIVATE);
            }

            public LiveData<UserDetails.Sub> getUserData() {
                return _userData;
            }

            public LiveData<String> getErrorMessage() {
                return _errorMessage;
            }

            public void updateUserInterface(boolean forceUpdate) {
                if (forceUpdate) {
                    fetchUserDetails();
                } else {
                    long lastUpdateTime = sharedPreferences.getLong("LastUpdateTime", 0);
                    boolean shouldFetchFromApi = System.currentTimeMillis() - lastUpdateTime > CACHE_DURATION;

                    if (shouldFetchFromApi) {
                        fetchUserDetails();
                    } else {
                        loadDataFromSharedPreferences();
                    }
                }
            }



            private void loadDataFromSharedPreferences() {
                String nombre = sharedPreferences.getString("UserNombre", "");
                String apellido = sharedPreferences.getString("UserApellido", "");
                String email = sharedPreferences.getString("UserEmail", "");
                String telefono = sharedPreferences.getString("UserTelefono", "");

                UserDetails.Sub userData = new UserDetails.Sub();
                userData.setNombre(nombre);
                userData.setApellido(apellido);
                userData.setEmail(email);
                userData.setTelefono(telefono);

                _userData.setValue(userData);
                Log.d("PerfilViewModel", "Datos cargados desde SharedPreferences: " + userData);
            }

            private void fetchUserDetails() {
                String token = sharedPreferences.getString("UserToken", null);

                if (token == null) {
                    _errorMessage.setValue("Token no encontrado");
                    loadDataFromSharedPreferences();
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
                                updateSharedPreferences(sub);
                                _userData.setValue(sub);
                                Log.d("PerfilViewModel", "Datos actualizados desde API: " + sub.toString());
                            } else {
                                _errorMessage.setValue("Datos de usuario no disponibles");
                                loadDataFromSharedPreferences();
                            }
                        } else {
                            _errorMessage.setValue("Error al obtener detalles de usuario: " + response.code());
                            loadDataFromSharedPreferences();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDetails> call, Throwable t) {
                        _errorMessage.setValue("Falló la conexión: " + t.getMessage());
                        loadDataFromSharedPreferences();
                    }
                });
            }

            private void updateSharedPreferences(UserDetails.Sub sub) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("UserNombre", sub.getNombre());
                editor.putString("UserApellido", sub.getApellido());
                editor.putString("UserEmail", sub.getEmail());
                editor.putString("UserTelefono", sub.getTelefono());
                editor.putString("UserNumIdentificacion", sub.getNumIdentificacion());
                editor.putLong("LastUpdateTime", System.currentTimeMillis());
                editor.apply();

                Log.d("PerfilViewModel", "Datos actualizados y guardados en SharedPreferences: " + sub.toString());
            }
        }