package com.example.gestorcitas.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestorcitas.R
import com.example.gestorcitas.model.ServicioCita

class ServicioCitaAdapter(private val lista: List<ServicioCita>) :
    RecyclerView.Adapter<ServicioCitaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPaciente: TextView = view.findViewById(R.id.tvPaciente)
        val tvMedico: TextView = view.findViewById(R.id.tvMedico)
        val tvEspecialidad: TextView = view.findViewById(R.id.tvEspecialidad)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        val tvHora: TextView = view.findViewById(R.id.tvHora)
        val tvMotivo: TextView = view.findViewById(R.id.tvMotivo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_servicio, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cita = lista[position]
        holder.tvPaciente.text = "Paciente: ${cita.paciente}"
        holder.tvMedico.text = "Médico: ${cita.medico}"
        holder.tvEspecialidad.text = "Especialidad: ${cita.especialidad}"
        holder.tvFecha.text = "Fecha: ${cita.fecha}"
        holder.tvHora.text = "Hora: ${cita.hora}"
        holder.tvMotivo.text = "Motivo: ${cita.motivo}"
    }
}
