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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerCitasMedicoActivity : AppCompatActivity() {

    private val listaCitas = mutableListOf<CitaMedico>()
    private lateinit var adapter: CitaMedicoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_citas_medico)

        val etMedicoId = findViewById<EditText>(R.id.etMedicoId)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val recycler = findViewById<RecyclerView>(R.id.recyclerCitasMedico)

        recycler.layoutManager = LinearLayoutManager(this)
        adapter = CitaMedicoAdapter(listaCitas)
        recycler.adapter = adapter

        btnBuscar.setOnClickListener {
            val id = etMedicoId.text.toString().toIntOrNull()
            if (id == null) {
                Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val api = ApiClient.retrofit.create(ApiService::class.java)
            api.listarCitasMedico(id).enqueue(object : Callback<List<CitaMedico>> {
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
}
