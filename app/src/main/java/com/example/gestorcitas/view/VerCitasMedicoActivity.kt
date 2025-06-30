package com.example.gestorcitas.view

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestorcitas.R
import com.example.gestorcitas.controller.ApiClient
import com.example.gestorcitas.controller.ApiService
import com.example.gestorcitas.model.CitaMedico
import com.example.gestorcitas.model.Medico
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerCitasMedicoActivity : AppCompatActivity() {

    private val listaCitas = mutableListOf<CitaMedico>()
    private lateinit var adapter: CitaMedicoAdapter

    private lateinit var spinnerMedicos: Spinner
    private var listaMedicos = listOf<Medico>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_citas_medico)

        spinnerMedicos = findViewById(R.id.spinnerMedicos)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val recycler = findViewById<RecyclerView>(R.id.recyclerCitasMedico)

        recycler.layoutManager = LinearLayoutManager(this)
        adapter = CitaMedicoAdapter(listaCitas)
        recycler.adapter = adapter

        cargarMedicos()

        btnBuscar.setOnClickListener {
            val index = spinnerMedicos.selectedItemPosition
            if (index < 0 || index >= listaMedicos.size) {
                Toast.makeText(this, "Seleccioná un médico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val medicoId = listaMedicos[index].id

            val api = ApiClient.retrofit.create(ApiService::class.java)
            api.listarCitasMedico(medicoId).enqueue(object : Callback<List<CitaMedico>> {
                override fun onResponse(call: Call<List<CitaMedico>>, response: Response<List<CitaMedico>>) {
                    listaCitas.clear()
                    val citas = response.body()
                    if (citas.isNullOrEmpty()) {
                        Toast.makeText(this@VerCitasMedicoActivity, "No hay citas asignadas", Toast.LENGTH_SHORT).show()
                    } else {
                        listaCitas.addAll(citas)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<List<CitaMedico>>, t: Throwable) {
                    Toast.makeText(this@VerCitasMedicoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun cargarMedicos() {
        val api = ApiClient.retrofit.create(ApiService::class.java)
        api.listarMedicos().enqueue(object : Callback<List<Medico>> {
            override fun onResponse(call: Call<List<Medico>>, response: Response<List<Medico>>) {
                if (response.isSuccessful) {
                    listaMedicos = response.body() ?: listOf()
                    val nombres = listaMedicos.map { it.nombre }
                    val adapter = ArrayAdapter(this@VerCitasMedicoActivity, android.R.layout.simple_spinner_item, nombres)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerMedicos.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Medico>>, t: Throwable) {
                Toast.makeText(this@VerCitasMedicoActivity, "Error al cargar médicos", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
