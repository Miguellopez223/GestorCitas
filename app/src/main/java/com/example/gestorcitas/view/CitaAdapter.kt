package com.example.gestorcitas.view


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestorcitas.R
import com.example.gestorcitas.model.Cita


class CitaAdapter(private val citas: List<Cita>) :
    RecyclerView.Adapter<CitaAdapter.CitaViewHolder>() {

    class CitaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMedico: TextView = view.findViewById(R.id.tvMedico)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cita, parent, false)
        return CitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citas[position]
        holder.tvMedico.text = "Médico: ${cita.medico}"
        holder.tvFecha.text = "Fecha: ${cita.fecha}"
    }

    override fun getItemCount(): Int = citas.size
}
