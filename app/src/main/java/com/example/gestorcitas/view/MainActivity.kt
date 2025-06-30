package com.example.gestorcitas.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.gestorcitas.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPaciente = findViewById<Button>(R.id.btnPaciente)
        val btnMedico = findViewById<Button>(R.id.btnMedico)

        btnPaciente.setOnClickListener {
            startActivity(Intent(this, MenuPacienteActivity::class.java))
        }

        btnMedico.setOnClickListener {
            startActivity(Intent(this, MenuMedicoActivity::class.java))
        }
    }
}
