package com.edinson.agroemnew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.modelApi.ProyectoDetails;
import com.edinson.agroemnew.modelApi.Seccion;

import java.util.List;

public class SeccionAdapter extends RecyclerView.Adapter<SeccionAdapter.SeccionViewHolder> {

    private List<Seccion> secciones;

    public SeccionAdapter(List<Seccion> secciones) {
        this.secciones = secciones;
    }

    @NonNull
    @Override
    public SeccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seccion, parent, false);
        return new SeccionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeccionViewHolder holder, int position) {
        Seccion seccion = secciones.get(position);

        // Construir el contenido basado en tipoSeccion
        StringBuilder contenido = new StringBuilder();
        if (seccion.getTipoSeccion() != null) {
            for (Seccion.TipoSeccion tipo : seccion.getTipoSeccion()) {
                contenido.append(tipo.getNombre() != null ? tipo.getNombre() : "Nombre no disponible")
                        .append(": ")
                        .append(tipo.getContenido() != null ? tipo.getContenido() : "Contenido no disponible")
                        .append("\n\n");
            }
        }

        // Como 'nombre' no está en 'Seccion', posiblemente quieras mostrar otra información aquí
        holder.nombreTextView.setText(seccion.getProyecto() != null ? seccion.getProyecto() : "Nombre no disponible");
        holder.contenidoTextView.setText(contenido.toString());
    }

    @Override
    public int getItemCount() {
        return secciones.size();
    }

    public static class SeccionViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView contenidoTextView;

        public SeccionViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            contenidoTextView = itemView.findViewById(R.id.contenidoTextView);
        }
    }
}