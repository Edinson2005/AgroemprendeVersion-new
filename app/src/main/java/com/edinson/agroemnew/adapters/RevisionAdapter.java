package com.edinson.agroemnew.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.proyecto.Revision;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RevisionAdapter extends RecyclerView.Adapter<RevisionAdapter.RevisionViewHolder> {
    private List<Revision> revisiones;

    public RevisionAdapter(List<Revision> revisiones) {
        this.revisiones = revisiones;
    }

    @NonNull
    @Override
    public RevisionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_revision, parent, false);
        return new RevisionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RevisionViewHolder holder, int position) {
        Revision revision = revisiones.get(position);
        holder.estadoTextView.setText(revision.getEstado());
        holder.tituloTextView.setText(revision.getTitulo());
        holder.sugerenciaTextView.setText(revision.getSugerencia());
        holder.descripcionTextView.setText(revision.getDescripcion());

        // Formatear fecha
        Date fechaRevision = revision.getFechaRevision(); // Asumiendo que es un Date
        String fechaFormateada = formatFecha(fechaRevision);
        holder.fechaRevisionTextView.setText(fechaFormateada);

        // Cambiar el color del fondo según su estado (Aprobado-Rechazado)
        String estado = revision.getEstado().toLowerCase();
        int color;
        if (estado.equalsIgnoreCase("aprobado")) {
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.Aprovado);
            holder.iconoEstadoImageView.setImageResource(R.drawable.img_10);
            holder.iconoEstadoImageView.setVisibility(View.VISIBLE);
        } else if (estado.equalsIgnoreCase("desaprobado")) {
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.Rechazado);
            holder.iconoEstadoImageView.setImageResource(R.drawable.img_11);
            holder.iconoEstadoImageView.setVisibility(View.VISIBLE);
        } else {
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.white);
            holder.iconoEstadoImageView.setVisibility(View.GONE);
        }

        holder.itemView.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return revisiones.size();
    }

    public static class RevisionViewHolder extends RecyclerView.ViewHolder {
        TextView estadoTextView;
        TextView tituloTextView;
        TextView sugerenciaTextView;
        TextView descripcionTextView;
        TextView fechaRevisionTextView;
        ImageView iconoEstadoImageView; // ImageView para el icono de estado

        public RevisionViewHolder(@NonNull View itemView) {
            super(itemView);
            estadoTextView = itemView.findViewById(R.id.estadoTextView);
            tituloTextView = itemView.findViewById(R.id.tituloTextView);
            sugerenciaTextView = itemView.findViewById(R.id.sugerenciaTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionTextView);
            fechaRevisionTextView = itemView.findViewById(R.id.fechaRevisionTextView);
            iconoEstadoImageView = itemView.findViewById(R.id.aprobadoimagen); // Enlazar el ImageView
        }
    }

    // Cambiar formatFecha para aceptar un Date
    private String formatFecha(Date fecha) {
        SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatoSalida.format(fecha);
    }
}
