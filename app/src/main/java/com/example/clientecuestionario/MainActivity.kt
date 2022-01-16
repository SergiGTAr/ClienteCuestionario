package com.example.clientecuestionario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btnEnviar(view: View) {
        val dbinstance = FirebaseDatabase.getInstance("https://servidorquestionari-default-rtdb.europe-west1.firebasedatabase.app")
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("partida")
        val id = myRef.child("id").getValue()
    }
}
