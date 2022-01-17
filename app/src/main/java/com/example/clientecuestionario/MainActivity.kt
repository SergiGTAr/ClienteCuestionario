package com.example.clientecuestionario

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    var idPartidaServidor: String = ""
    var idPartidaServer: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btnEnviar(view: View) {
        var coincideixID = false
        val txtIDPartida = findViewById<TextView>(R.id.txtIDPartida)
        val dbinstance = FirebaseDatabase.getInstance("https://servidorquestionari-default-rtdb.europe-west1.firebasedatabase.app")
        val database = dbinstance.getReference("")
        database.child("partida").child("id").get().addOnSuccessListener {
            if(it.value.toString() == txtIDPartida.text.toString()) {
                coincideixID = true
            }
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
        if(coincideixID){
            Toast.makeText(this, "Conté el valor", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No conté el valor", Toast.LENGTH_SHORT).show()
        }
    }
}