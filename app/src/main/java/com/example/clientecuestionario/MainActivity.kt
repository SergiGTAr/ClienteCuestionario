package com.example.clientecuestionario

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {
    var idPartidaServidor: String = ""
    var idPartidaServer: String = ""
    var idPartidaActual: String = ""
    var NomEquip = ""
    var retornval = false
    var numeroEquipsActual = 0
    private val dbinstance =
        FirebaseDatabase.getInstance("https://servidorquestionari-default-rtdb.europe-west1.firebasedatabase.app")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun comprovarEstatPartida(database: DatabaseReference, item: DataSnapshot): Boolean {
        database.child("partides").child("${item.key}").child("estatpartida")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value == "esperant") {
                        retornval = false
                    } else if (p0.value == "encurs") {
                        retornval = true
                    }
                }
            })
        return retornval
    }

    fun btnEnviar(view: View) {
        val txtIDPartida = findViewById<TextView>(R.id.txtIDPartida)
        val txtNomEquip = findViewById<TextView>(R.id.txtNomEquip)
        val btnEnviarobj = findViewById<TextView>(R.id.btnEnviar)
        NomEquip = txtNomEquip.text.toString()
        idPartidaActual = txtIDPartida.text.toString()
        if (NomEquip == "") {
            NomEquip = (100..999).random().toString()
        }
        val database = dbinstance.getReference("")
        database.child("partides").child(idPartidaActual).child("equips").child("numequips").addListenerForSingleValueEvent(
            object : ValueEventListener {
                //mDatabase.child("users").addValueEventListener(new ValueEventListener() {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val items: Iterator<DataSnapshot> = dataSnapshot.children.iterator()
                    while (items.hasNext()) {
                        val item = items.next()
                        Toast.makeText(this@MainActivity, item.value.toString(), Toast.LENGTH_SHORT).show()
                        numeroEquipsActual = item.value.toString().toInt()
                    }
                    database.child("partides").child(idPartidaActual).child("equips").child("numequips").removeEventListener(this)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            }
        )

        if (numeroEquipsActual >= 2){
            Toast.makeText(this, "La partida està plena", Toast.LENGTH_SHORT).show()
            return
        }



        database.child("partides").addListenerForSingleValueEvent(
            object : ValueEventListener {
                //mDatabase.child("users").addValueEventListener(new ValueEventListener() {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val items: Iterator<DataSnapshot> = dataSnapshot.children.iterator()
                    while (items.hasNext()) {
                        val item = items.next()
                        val partidaEmpezada = comprovarEstatPartida(database, item)
                        if (item.key == txtIDPartida.text.toString() && !partidaEmpezada) {
                            Toast.makeText(
                                this@MainActivity,
                                "ENCONTRADA PARTIDA",
                                Toast.LENGTH_SHORT
                            ).show()
                            database.child("partides").child("${item.key}").child("equips")
                                .child("numequips").setValue(
                                    ServerValue.increment(1)
                                )
                            database.child("partides").child("${item.key}").child("equips")
                                .child(NomEquip).child("Puntuació").setValue(0)
                            database.child("partides").child("${item.key}").child("equips")
                                .child(NomEquip).child("Iteració").setValue(1)
                            btnEnviarobj.visibility = INVISIBLE
                            Toast.makeText(
                                this@MainActivity,
                                "ESPERANT INICI PARTIDA",
                                Toast.LENGTH_LONG
                            ).show()
                            esperantIniciPartida(database)
                        }
                    }
                    database.child("partides").removeEventListener(this)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            }
        )
//        var coincideixID = false
//        val txtIDPartida = findViewById<TextView>(R.id.txtIDPartida)
//        val dbinstance = FirebaseDatabase.getInstance("https://servidorquestionari-default-rtdb.europe-west1.firebasedatabase.app")
//        val database = dbinstance.getReference("")
//        database.child("partida").child("83702").get().addOnSuccessListener {
//            if(it.value.toString() == txtIDPartida.text.toString()) {
//                coincideixID = true
//            }
//            Log.i("firebase", "Got value ${it.value}")
//        }.addOnFailureListener{
//            Log.e("firebase", "Error getting data", it)
//        }
//        if(coincideixID){
//            Toast.makeText(this, "Conté el valor", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "No conté el valor", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun esperantIniciPartida(postReference: DatabaseReference) {
        postReference.child("partides").child(idPartidaActual).child("estatpartida")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val estat = dataSnapshot.getValue(String::class.java)
                    if (estat == "encurs") {
                        partidaIniciada(postReference)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Do something
                }
            })
    }

    private fun partidaIniciada(dbReference: DatabaseReference) {

        dbReference.child("partides").child(idPartidaActual).child("estatpartida")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val estat = dataSnapshot.getValue(String::class.java)
                    if (estat == "encurs") {
                        setContentView(R.layout.quiz)

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Do something
                }
            })
    }
}