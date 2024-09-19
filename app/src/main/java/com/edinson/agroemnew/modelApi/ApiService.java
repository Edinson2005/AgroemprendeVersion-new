package com.edinson.agroemnew.modelApi;

import com.edinson.agroemnew.modelApi.notificaciones.Convocatoria;
import com.edinson.agroemnew.modelApi.notificaciones.NotiConvocatorias;
import com.edinson.agroemnew.modelApi.notificaciones.ProyectoNot;
import com.edinson.agroemnew.modelApi.notificaciones.TokenRequest;
import com.edinson.agroemnew.modelApi.proyecto.Project;
import com.edinson.agroemnew.modelApi.proyecto.ProyectoDetails;
import com.edinson.agroemnew.modelApi.proyecto.RegisterRequest;
import com.edinson.agroemnew.modelApi.usuario.Token;
import com.edinson.agroemnew.modelApi.usuario.UserDetails;
import com.edinson.agroemnew.modelApi.usuario.UserUpdate;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import java.util.List;
import java.util.Map;

import retrofit2.http.Path;

public interface ApiService {

    ////Usuario
    @FormUrlEncoded
    @POST("auth/login")
    Call<Token> getlogin(
            @Field("email") String email,
            @Field("contrasena") String contrasena);

    @POST("auth/usuario")
    Call<Void> registerUser (@Body RegisterRequest registerRequest);
    @GET("/auth/profile")
    Call<UserDetails> getUserDetails(@Header("Authorization") String token);

    @HTTP(method = "PATCH", path = "auth/usuario/{userId}", hasBody = true)
    Call<Void> updateUserProfile(
            @Path("userId") String userId, // Usa "userId" para coincidir con el parámetro en la URL
            @Header("Authorization") String token,
            @Body UserUpdate userUpdateRequest
    );
    //RECUPERAR CONTRASEÑA
    @POST ("auth/forgot-password")
    Call<Forgot> forgotPassword(@Body Forgot forgot);


    ////PROYECTOS

    //DETALLES DE PROYECTOS
    @GET("proyectos/{id}")
    Call<ProyectoDetails> getProyectoDetails(@Header("Authorization") String token, @Path("id") String id);

    //TODOS LOS MIS PROYECTOS
    @GET("/proyectos/mis-proyectos")
    Call<List<Project>> getPrueba(@Header("Authorization") String token);



    /////NOTIFICACIONES

    // Métodos para Notificaciones
    @GET("/notificaciones/convocatoria")
    Call<List<NotiConvocatorias>> getNotificaciones(@Header("Authorization") String token);

    @GET("/convocatoria/{id}")
    Call<Convocatoria> getConvocatoria(@Header("Authorization") String token, @Path("id") String convocatoriaId);


    @GET("/notificaciones/mis-proyectos")
    Call<List<ProyectoNot>> getProyectos(@Header("Authorization") String token);

    @POST("notificaciones/send")
    Call<Void> enviarToken(@Body TokenRequest tokenRequest);

    @PATCH("/auth/usuario/{id}")
    Call<Void> actualizarDeviceToken(@Path("id") String userId, @Body Map<String, String> deviceToken);

}
