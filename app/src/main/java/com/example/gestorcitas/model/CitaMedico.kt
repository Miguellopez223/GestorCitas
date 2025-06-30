package com.example.gestorcitas.model

data class CitaMedico(
    val id: Int,
    val paciente: String,
    val fecha: String,
    val hora: String,
    val motivo: String
)
