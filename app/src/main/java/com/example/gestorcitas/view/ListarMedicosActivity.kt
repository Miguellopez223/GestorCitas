package com.example.gestorcitas.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestorcitas.R
import com.example.gestorcitas.controller.ApiClient
import com.example.gestorcitas.controller.ApiService
import com.example.gestorcitas.model.Medico
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListarMedicosActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: MedicoAdapter
    private val listaMedicos = mutableListOf<Medico>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_medicos)

        recycler = findViewById(R.id.recyclerMedicos)
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = MedicoAdapter(listaMedicos)
        recycler.adapter = adapter

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }


        val api = ApiClient.retrofit.create(ApiService::class.java)
        api.listarMedicos().enqueue(object : Callback<List<Medico>> {
            override fun onResponse(call: Call<List<Medico>>, response: Response<List<Medico>>) {
                if (response.isSuccessful) {
                    listaMedicos.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Medico>>, t: Throwable) {
                Toast.makeText(this@ListarMedicosActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
