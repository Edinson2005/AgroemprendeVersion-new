package com.edinson.agroemnew.ui.proyecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.proyecto.Project;

import java.util.List;

public class ProyectoAdapter extends RecyclerView.Adapter<ProyectoAdapter.ViewHolder> {
    private Context context;
    private List<Project> projectList;
    private OnItemClickListener onItemClickListener;

    public ProyectoAdapter(Context context, List<Project> projectList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.projectList = projectList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String pruebaId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_proyecto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Project project = projectList.get(position);
        holder.bind(project);
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tituloTextView, estadoTextView, fechaTextView, descripcionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.tituloTextView);
            fechaTextView = itemView.findViewById(R.id.fechaTextView);
            estadoTextView = itemView.findViewById(R.id.estadoTextView);
            //descripcionTextView = itemView.findViewById(R.id.descripcionTextView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Project project = projectList.get(position);
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(project.get_id());
                    }
                }
            });
        }

        public void bind(Project project) {
            tituloTextView.setText(project.getTitulo());
            fechaTextView.setText(project.getFecha());
            estadoTextView.setText(project.getEstado());
          ////  descripcionTextView.setText(project.getDescripcion());

            String estado = project.getEstado().toLowerCase();
            if (estado.contains("en progreso")) {
                int color = context.getResources().getColor(R.color.En_progreso);
                estadoTextView.setTextColor(color);
            } else if (estado.contains("revisado con errores")) {
                int color = context.getResources().getColor(R.color.Error);
                estadoTextView.setTextColor(color);
            } else if (estado.contains("revisado")) {
                int color = context.getResources().getColor(R.color.revisado);
                estadoTextView.setTextColor(color);
            } else {
                int color = context.getResources().getColor(R.color.black);
                estadoTextView.setTextColor(color);
            }
        }
    }
}