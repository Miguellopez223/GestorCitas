package com.example.gestorcitas.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestorcitas.R
import com.example.gestorcitas.controller.ApiClient
import com.example.gestorcitas.controller.ApiService
import com.example.gestorcitas.model.Cita
import com.example.gestorcitas.model.Respuesta
import com.example.gestorcitas.model.RespuestaBuscarPaciente
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerCitasActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CitaAdapter
    private val listaCitas = mutableListOf<Cita>()
    private var pacienteId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_citas)

        val etCi = findViewById<EditText>(R.id.etCi)
        val btnBuscarCitas = findViewById<Button>(R.id.btnBuscarCitas)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        recyclerView = findViewById(R.id.recyclerCitas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CitaAdapter(listaCitas) { cita ->
            AlertDialog.Builder(this)
                .setTitle("¿Cancelar cita?")
                .setMessage("¿Deseás cancelar la cita con ${cita.medico} el ${cita.fecha}?")
                .setPositiveButton("Sí") { _, _ -> eliminarCita(cita) }
                .setNegativeButton("No", null)
                .show()
        }

        recyclerView.adapter = adapter

        btnVolver.setOnClickListener {
            val intent = Intent(this, MenuPacienteActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        btnBuscarCitas.setOnClickListener {
            val ciIngresado = etCi.text.toString()
            if (ciIngresado.isBlank()) {
                Toast.makeText(this, "Ingresá tu CI", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val api = ApiClient.retrofit.create(ApiService::class.java)
            api.buscarPaciente(ciIngresado).enqueue(object : Callback<RespuestaBuscarPaciente> {
                override fun onResponse(call: Call<RespuestaBuscarPaciente>, response: Response<RespuestaBuscarPaciente>) {
                    val r = response.body()
                    if (r?.success == true) {
                        pacienteId = r.id
                        cargarCitas()
                    } else {
                        Toast.makeText(this@VerCitasActivity, r?.mensaje ?: "No encontrado", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RespuestaBuscarPaciente>, t: Throwable) {
                    Toast.makeText(this@VerCitasActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun cargarCitas() {
        val id = pacienteId ?: return
        val api = ApiClient.retrofit.create(ApiService::class.java)
        api.listarCitasPaciente(id).enqueue(object : Callback<List<Cita>> {
            override fun onResponse(call: Call<List<Cita>>, response: Response<List<Cita>>) {
                if (response.isSuccessful) {
                    listaCitas.clear()
                    response.body()?.let { listaCitas.addAll(it) }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@VerCitasActivity, "Error al obtener las citas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Cita>>, t: Throwable) {
                Toast.makeText(this@VerCitasActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminarCita(cita: Cita) {
        val api = ApiClient.retrofit.create(ApiService::class.java)
        api.eliminarCita(cita.id).enqueue(object : Callback<Respuesta> {
            override fun onResponse(call: Call<Respuesta>, response: Response<Respuesta>) {
                val r = response.body()
                if (r?.success == true) {
                    Toast.makeText(this@VerCitasActivity, "Cita cancelada", Toast.LENGTH_SHORT).show()
                    listaCitas.remove(cita)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@VerCitasActivity, "No se pudo cancelar", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Respuesta>, t: Throwable) {
                Toast.makeText(this@VerCitasActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
