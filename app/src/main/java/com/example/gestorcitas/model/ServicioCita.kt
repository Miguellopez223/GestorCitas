package com.example.gestorcitas.model

data class ServicioCita(
    val id: Int,
    val paciente: String,
    val medico: String,
    val especialidad: String,
    val fecha: String,
    val hora: String,
    val motivo: String
)
