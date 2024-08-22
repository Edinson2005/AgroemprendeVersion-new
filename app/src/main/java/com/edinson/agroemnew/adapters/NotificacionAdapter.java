package com.edinson.agroemnew.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.NotiConvocatorias;

import java.util.List;
public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder> {

    private List<NotiConvocatorias> notificaciones;
    private Context context;

    public NotificacionAdapter(List<NotiConvocatorias> notificaciones, Context context) {
        this.notificaciones = notificaciones;
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
        if (notificaciones != null && position < notificaciones.size()) {
            NotiConvocatorias notiConvocatorias = notificaciones.get(position);
            holder.titulo.setText(notiConvocatorias.getTitle());
            holder.cuerpo.setText(notiConvocatorias.getBody());
            holder.url.setText(notiConvocatorias.getUrl());
            holder.estado.setText(notiConvocatorias.getEstado());

            // No aplicar color adicional, usar el color de fondo predeterminado
            // Puedes hacer esto con un fondo transparente
            // holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

            // Si quieres que el color de fondo sea el predeterminado sin aplicar cambios
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

            // Evento click para marcar la notificación como vista
            holder.itemView.setOnClickListener(v -> {
                // Guardar el estado como 'vista' en SharedPreferences
                guardarEstadoVista(notiConvocatorias.getId(), true);

                // Cambiar el color del fondo inmediatamente
                // Puedes comentar esta línea si no deseas cambiar el color en el clic
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

                // Notificar que el ítem ha cambiado
                notifyItemChanged(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return notificaciones != null ? notificaciones.size() : 0;
    }

    private void guardarEstadoVista(String idNotificacion, boolean vista) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Notificaciones", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("vista_" + idNotificacion, vista);
        editor.apply();
    }

    private boolean cargarEstadoVista(String idNotificacion) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Notificaciones", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("vista_" + idNotificacion, false); // false por defecto si no existe
    }

    public static class NotificacionViewHolder extends RecyclerView.ViewHolder {
        TextView titulo;
        TextView cuerpo;
        TextView url;
        TextView estado;

        public NotificacionViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titleNotificacion);
            cuerpo = itemView.findViewById(R.id.bodyNotificacion);
            url = itemView.findViewById(R.id.urlNotificacion);
            estado = itemView.findViewById(R.id.estadoconvocatoria);
        }
    }
}