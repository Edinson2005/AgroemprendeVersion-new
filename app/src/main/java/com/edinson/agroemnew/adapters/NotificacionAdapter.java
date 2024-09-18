package com.edinson.agroemnew.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.DetalleConvocatoria;
import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.notificaciones.NotiConvocatorias;

import java.util.ArrayList;
import java.util.List;
public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder> {

    private List<NotiConvocatorias> notificaciones;
    private Context context;

    public NotificacionAdapter(Context context) {
        this.notificaciones = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion, parent, false);
        return new NotificacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionViewHolder holder, int position) {
        NotiConvocatorias notiConvocatorias = notificaciones.get(position);
        holder.titulo.setText(notiConvocatorias.getTitle());
        holder.cuerpo.setText(notiConvocatorias.getBody());

        holder.itemView.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyApp", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("UserToken", null);

            Intent intent = new Intent(context, DetalleConvocatoria.class);
            intent.putExtra("convocatoria_id", notiConvocatorias.getConvocatoria().get_id());
            intent.putExtra("convocatoria_title", notiConvocatorias.getConvocatoria().getTitle());
            intent.putExtra("convocatoria_descripcion", notiConvocatorias.getConvocatoria().getDescripcion());
            intent.putExtra("convocatoria_fechaInicio", notiConvocatorias.getConvocatoria().getFechaInicio());
            intent.putExtra("authorization", token);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    public void setNotificaciones(List<NotiConvocatorias> nuevasNotificaciones) {
        this.notificaciones.clear();
        this.notificaciones.addAll(nuevasNotificaciones);
        notifyDataSetChanged();
    }

    public static class NotificacionViewHolder extends RecyclerView.ViewHolder {
        TextView titulo;
        TextView cuerpo;

        public NotificacionViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titleNotificacion);
            cuerpo = itemView.findViewById(R.id.bodyNotificacion);
        }
    }
}