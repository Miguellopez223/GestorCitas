package com.example.gestorcitas.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestorcitas.R
import com.example.gestorcitas.model.Medico

class MedicoAdapter(private val lista: List<Medico>) :
    RecyclerView.Adapter<MedicoAdapter.MedicoViewHolder>() {

    class MedicoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tvNombreMedico)
        val especialidad: TextView = view.findViewById(R.id.tvEspecialidad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medico, parent, false)
        return MedicoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicoViewHolder, position: Int) {
        val medico = lista[position]
        holder.nombre.text = medico.nombre
        holder.especialidad.text = medico.especialidad
    }

    override fun getItemCount(): Int = lista.size
}
