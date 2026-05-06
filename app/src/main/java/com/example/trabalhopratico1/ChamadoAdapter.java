package com.example.trabalhopratico1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChamadoAdapter extends RecyclerView.Adapter<ChamadoAdapter.ChamadoViewHolder> {

    private ArrayList<Chamado> lista;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Chamado chamadoClicado);
    }

    public ChamadoAdapter(ArrayList<Chamado> lista, Context context, OnItemClickListener listener) {
        this.lista = lista;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChamadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chamado, parent, false);
        return new ChamadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChamadoViewHolder holder, int position) {
        Chamado chamadoAtual = lista.get(position);

        holder.txtTitulo.setText(chamadoAtual.getTitulo());
        holder.txtData.setText(chamadoAtual.getData());
        holder.txtLocal.setText("Local: " + chamadoAtual.getLocal());
        holder.txtTipo.setText("Tipo: " + chamadoAtual.getTipo());
        holder.txtStatus.setText("Status: " + chamadoAtual.getStatus());

        switch (chamadoAtual.getStatus()) {
            case "Aberto":
                holder.txtStatus.setTextColor(Color.parseColor("#D32F2F")); // Vermelho
                break;
            case "Em Atendimento":
                holder.txtStatus.setTextColor(Color.parseColor("#F57C00")); // Laranja
                break;
            case "Concluído":
                holder.txtStatus.setTextColor(Color.parseColor("#388E3C")); // Verde
                break;
            default:
                holder.txtStatus.setTextColor(Color.parseColor("#1565C0")); // Azul
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(chamadoAtual);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public OnItemClickListener getListener() {
        return listener;
    }

    public static class ChamadoViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitulo, txtData, txtLocal, txtTipo, txtStatus;

        public ChamadoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtItemTitulo);
            txtData = itemView.findViewById(R.id.txtItemData);
            txtLocal = itemView.findViewById(R.id.txtItemLocal);
            txtTipo = itemView.findViewById(R.id.txtItemTipo);
            txtStatus = itemView.findViewById(R.id.txtItemStatus);
        }
    }
}