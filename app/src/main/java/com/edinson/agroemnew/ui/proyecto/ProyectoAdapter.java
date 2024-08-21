package com.edinson.agroemnew.ui.proyecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.Proyecto;

import java.util.List;

public class ProyectoAdapter extends RecyclerView.Adapter<ProyectoAdapter.ViewHolder> {
    private Context context;
    private List<Proyecto> proyectoList;
    private OnItemClickListener onItemClickListener;

    public ProyectoAdapter(Context context, List<Proyecto> proyectoList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.proyectoList = proyectoList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String proyectoId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_proyecto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Proyecto proyecto = proyectoList.get(position);
        holder.bind(proyecto);
    }

    @Override
    public int getItemCount() {
        return proyectoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tituloTextView, estadoTextView,fechaTextView, descripcionTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.tituloTextView);
            fechaTextView = itemView.findViewById(R.id.fechaTextView);
            estadoTextView = itemView.findViewById(R.id.estadoTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionTextView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Proyecto proyecto = proyectoList.get(position);
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(proyecto.get_id());
                    }
                }
            });
        }

        public void bind(Proyecto proyecto) {
            tituloTextView.setText(proyecto.getTitulo());
            fechaTextView.setText(proyecto.getFecha());
            estadoTextView.setText(proyecto.getEstado());
            descripcionTextView.setText(proyecto.getDescripcion());

            String estado = proyecto.getEstado().toLowerCase();
            if (estado.contains("en progreso")) {
                estadoTextView.setTextColor(context.getResources().getColor(R.color.En_progreso));
            } else if (estado.contains("revisado")) {
                estadoTextView.setTextColor(context.getResources().getColor(R.color.revisado));
            } else {
                estadoTextView.setTextColor(context.getResources().getColor(R.color.black));
            }
        }
    }
}