package com.example.gestorcitas.model

data class Cita(
    val id: Int,
    val paciente_id: Int,
    val medico_id: Int,
    val medico: String,
    val fecha: String
)
