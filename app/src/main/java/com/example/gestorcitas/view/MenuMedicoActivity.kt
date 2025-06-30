package com.example.gestorcitas.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.gestorcitas.R

class MenuMedicoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_medico)

        val btnRegistrar = findViewById<Button>(R.id.btnRegistrarMedico)
        val btnVerCitas = findViewById<Button>(R.id.btnVerCitasMedico)
        val btnServicios = findViewById<Button>(R.id.btnVerServicios)
        val btnVolver = findViewById<Button>(R.id.btnVolverPrincipal)

        btnRegistrar.setOnClickListener {
            startActivity(Intent(this, InsertarMedicoActivity::class.java))
        }

        btnVerCitas.setOnClickListener {
            startActivity(Intent(this, VerCitasMedicoActivity::class.java))
        }

        btnServicios.setOnClickListener {
            // Aquí después crearemos ConsultarServiciosActivity
            // Por ahora no hace nada
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}
