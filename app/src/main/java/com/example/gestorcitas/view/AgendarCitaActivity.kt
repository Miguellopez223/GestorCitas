package com.example.gestorcitas.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.gestorcitas.R
import com.example.gestorcitas.controller.ApiClient
import com.example.gestorcitas.controller.ApiService
import com.example.gestorcitas.model.Medico
import com.example.gestorcitas.model.Respuesta
import com.example.gestorcitas.model.RespuestaBuscarPaciente
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgendarCitaActivity : AppCompatActivity() {

    private lateinit var spinnerMedicos: Spinner
    private lateinit var etFecha: EditText
    private lateinit var etHora: EditText
    private lateinit var etMotivo: EditText
    private lateinit var btnAgendar: Button
    private lateinit var etCi: EditText
    private lateinit var btnBuscar: Button
    private lateinit var btnVolver: Button

    private var pacienteId: Int? = null
    private var listaMedicos = listOf<Medico>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendar_cita)

        spinnerMedicos = findViewById(R.id.spinnerMedicos)
        etFecha = findViewById(R.id.etFecha)
        etHora = findViewById(R.id.etHora)
        etMotivo = findViewById(R.id.etMotivo)
        btnAgendar = findViewById(R.id.btnAgendar)
        etCi = findViewById(R.id.etCi)
        btnBuscar = findViewById(R.id.btnBuscar)
        btnVolver = findViewById(R.id.btnVolver)

        desactivarFormulario()

        btnBuscar.setOnClickListener {
            val ci = etCi.text.toString().trim()
            if (ci.isBlank()) {
                Toast.makeText(this, "Ingresá tu CI", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val api = ApiClient.retrofit.create(ApiService::class.java)
            api.buscarPaciente(ci).enqueue(object : Callback<RespuestaBuscarPaciente> {
                override fun onResponse(call: Call<RespuestaBuscarPaciente>, response: Response<RespuestaBuscarPaciente>) {
                    val r = response.body()
                    if (r?.success == true) {
                        pacienteId = r.id
                        Toast.makeText(this@AgendarCitaActivity, "Paciente: ${r.nombre}", Toast.LENGTH_SHORT).show()
                        habilitarFormulario()
                        cargarMedicos()
                    } else {
                        Toast.makeText(this@AgendarCitaActivity, r?.mensaje ?: "No encontrado", Toast.LENGTH_SHORT).show()
                        desactivarFormulario()
                    }
                }

                override fun onFailure(call: Call<RespuestaBuscarPaciente>, t: Throwable) {
                    Toast.makeText(this@AgendarCitaActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnAgendar.setOnClickListener {
            val fecha = etFecha.text.toString().trim()
            val hora = etHora.text.toString().trim()
            val motivo = etMotivo.text.toString().trim()
            val medicoSeleccionado = spinnerMedicos.selectedItemPosition

            if (fecha.isBlank() || hora.isBlank() || motivo.isBlank() || pacienteId == null || medicoSeleccionado < 0) {
                Toast.makeText(this, "Completá todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val medicoId = listaMedicos[medicoSeleccionado].id
            val api = ApiClient.retrofit.create(ApiService::class.java)
            api.insertarCita(pacienteId!!, medicoId, fecha, hora, motivo)
                .enqueue(object : Callback<Respuesta> {
                    override fun onResponse(call: Call<Respuesta>, response: Response<Respuesta>) {
                        if (response.body()?.success == true) {
                            Toast.makeText(this@AgendarCitaActivity, "Cita agendada", Toast.LENGTH_SHORT).show()
                            etFecha.setText("")
                            etHora.setText("")
                            etMotivo.setText("")
                        } else {
                            Toast.makeText(this@AgendarCitaActivity, "Error al agendar", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Respuesta>, t: Throwable) {
                        Toast.makeText(this@AgendarCitaActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        btnVolver.setOnClickListener {
            finish() // volver atrás
        }
    }

    private fun habilitarFormulario() {
        btnAgendar.isEnabled = true
        spinnerMedicos.isEnabled = true
        etFecha.isEnabled = true
        etHora.isEnabled = true
        etMotivo.isEnabled = true
    }

    private fun desactivarFormulario() {
        btnAgendar.isEnabled = false
        spinnerMedicos.isEnabled = false
        etFecha.isEnabled = false
        etHora.isEnabled = false
        etMotivo.isEnabled = false
    }

    private fun cargarMedicos() {
        val api = ApiClient.retrofit.create(ApiService::class.java)
        api.listarMedicos().enqueue(object : Callback<List<Medico>> {
            override fun onResponse(call: Call<List<Medico>>, response: Response<List<Medico>>) {
                if (response.isSuccessful) {
                    listaMedicos = response.body() ?: listOf()
                    val nombres = listaMedicos.map { it.nombre }
                    val adapter = ArrayAdapter(this@AgendarCitaActivity, android.R.layout.simple_spinner_item, nombres)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerMedicos.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Medico>>, t: Throwable) {
                Toast.makeText(this@AgendarCitaActivity, "Error al cargar médicos", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
