package com.example.gestorcitas.model

data class RespuestaBuscarPaciente(
    val success: Boolean,
    val id: Int,
    val nombre: String,
    val mensaje: String
)
