package com.example.gestorcitas.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.gestorcitas.R

class MenuPacienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_paciente)

        val btnRegistro = findViewById<Button>(R.id.btnRegistro)
        val btnMedicos = findViewById<Button>(R.id.btnMedicos)
        val btnAgendar = findViewById<Button>(R.id.btnAgendar)
        val btnVerCitas = findViewById<Button>(R.id.btnVerCitas)
        val btnSalir = findViewById<Button>(R.id.btnSalir)

        btnRegistro.setOnClickListener {
            startActivity(Intent(this, InsertarPacienteActivity::class.java))
        }

        btnMedicos.setOnClickListener {
            startActivity(Intent(this, ListarMedicosActivity::class.java))
        }

        btnAgendar.setOnClickListener {
            startActivity(Intent(this, AgendarCitaActivity::class.java))
        }

        btnVerCitas.setOnClickListener {
            startActivity(Intent(this, VerCitasActivity::class.java))
        }

        btnSalir.setOnClickListener {
            finish()
        }
    }
}
