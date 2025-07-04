package com.example.gestorcitas.controller

import com.example.gestorcitas.model.Cita
import com.example.gestorcitas.model.CitaMedico
import com.example.gestorcitas.model.Medico
import com.example.gestorcitas.model.Respuesta
import com.example.gestorcitas.model.RespuestaBuscarPaciente
import com.example.gestorcitas.model.ServicioCita
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("insertar_paciente.php")
    fun insertarPaciente(
        @Field("nombre") nombre: String,
        @Field("ci") ci: String,
        @Field("telefono") telefono: String
    ): Call<Respuesta>

    @GET("listar_medicos.php")
    fun listarMedicos(): Call<List<Medico>>

    @FormUrlEncoded
    @POST("insertar_cita.php")
    fun insertarCita(
        @Field("paciente_id") pacienteId: Int,
        @Field("medico_id") medicoId: Int,
        @Field("fecha") fecha: String,
        @Field("hora") hora: String,
        @Field("motivo") motivo: String
    ): Call<Respuesta>

    @FormUrlEncoded
    @POST("listar_citas_paciente.php")
    fun listarCitasPaciente(
        @Field("paciente_id") pacienteId: Int
    ): Call<List<Cita>>

    @FormUrlEncoded
    @POST("eliminar_cita.php")
    fun eliminarCita(
        @Field("id") idCita: Int
    ): Call<Respuesta>

    @FormUrlEncoded
    @POST("buscar_paciente.php")
    fun buscarPaciente(@Field("ci") ci: String): Call<RespuestaBuscarPaciente>

    @FormUrlEncoded
    @POST("insertar_medico.php")
    fun insertarMedico(
        @Field("nombre") nombre: String,
        @Field("especialidad") especialidad: String
    ): Call<Respuesta>

    @FormUrlEncoded
    @POST("listar_citas_medico.php")
    fun listarCitasMedico(
        @Field("medico_id") medicoId: Int
    ): Call<List<CitaMedico>>

    @GET("listar_citas.php")
    fun listarServiciosPorFecha(
        @Query("inicio") inicio: String,
        @Query("fin") fin: String
    ): Call<List<ServicioCita>>

}
