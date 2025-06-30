package com.example.gestorcitas.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.gestorcitas.R
import com.example.gestorcitas.controller.ApiClient
import com.example.gestorcitas.controller.ApiService
import com.example.gestorcitas.model.Medico
import com.example.gestorcitas.model.Respuesta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AgendarCitaActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var fechaTexto: TextView
    private lateinit var medicos: List<Medico>
    private var medicoSeleccionadoId: Int? = null
    private var fechaSeleccionada: String = ""

    private val pacienteId = 1 // Simulado. En tu app real lo obtendrás tras login o registro.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendar_cita)

        spinner = findViewById(R.id.spinnerMedicos)
        val btnFecha = findViewById<Button>(R.id.btnFecha)
        fechaTexto = findViewById(R.id.tvFechaSeleccionada)
        val btnAgendar = findViewById<Button>(R.id.btnAgendar)

        val api = ApiClient.retrofit.create(ApiService::class.java)
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }


        // Cargar médicos
        api.listarMedicos().enqueue(object : Callback<List<Medico>> {
            override fun onResponse(call: Call<List<Medico>>, response: Response<List<Medico>>) {
                medicos = response.body() ?: emptyList()
                val nombres = medicos.map { it.nombre }
                val adapter = ArrayAdapter(this@AgendarCitaActivity, android.R.layout.simple_spinner_item, nombres)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                        medicoSeleccionadoId = medicos[position].id
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }
            }

            override fun onFailure(call: Call<List<Medico>>, t: Throwable) {
                Toast.makeText(this@AgendarCitaActivity, "Error al cargar médicos", Toast.LENGTH_SHORT).show()
            }
        })

        // Selector de fecha
        btnFecha.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                fechaSeleccionada = "$y-${m + 1}-$d"
                fechaTexto.text = "Fecha: $fechaSeleccionada"
            }, year, month, day)
            datePicker.show()
        }

        btnAgendar.setOnClickListener {
            if (medicoSeleccionadoId == null || fechaSeleccionada.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            api.insertarCita(pacienteId, medicoSeleccionadoId!!, fechaSeleccionada).enqueue(object : Callback<Respuesta> {
                override fun onResponse(call: Call<Respuesta>, response: Response<Respuesta>) {
                    val r = response.body()
                    Toast.makeText(this@AgendarCitaActivity, r?.mensaje ?: "Sin respuesta", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<Respuesta>, t: Throwable) {
                    Toast.makeText(this@AgendarCitaActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
