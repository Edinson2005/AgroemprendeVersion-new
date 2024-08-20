package com.edinson.agroemnew.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.ProyectoNot;

import java.util.List;
public class ProyectoAdapter extends RecyclerView.Adapter<ProyectoAdapter.ProyectoViewHolder> {

    private List<ProyectoNot> proyectos;

    public ProyectoAdapter(List<ProyectoNot> proyectos) {
        this.proyectos = proyectos;
    }

    @NonNull
    @Override
    public ProyectoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proyecto, parent, false);
        return new ProyectoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProyectoViewHolder holder, int position) {
        ProyectoNot proyecto = proyectos.get(position);
        holder.titulo.setText(proyecto.getTitulo());
        holder.fecha.setText(proyecto.getFecha());
        holder.descripcion.setText(proyecto.getDescripcion());
        holder.estado.setText(proyecto.getEstado());
    }


    @Override
    public int getItemCount() {
        return proyectos != null ? proyectos.size() : 0;
    }


    public static class ProyectoViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, fecha, descripcion, estado;

        public ProyectoViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tituloTextView);
            fecha = itemView.findViewById(R.id.fechaTextView);
            descripcion = itemView.findViewById(R.id.descripcionTextView);
            estado = itemView.findViewById(R.id.estadoTextView);
        }
    }
}