package com.example.practica3.ui.carrito

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.practica3.R
import com.example.practica3.ui.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_detalles.*

class DetallesActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var recoger = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles)

        val user = mAuth.currentUser
        cargarDireccion(user?.email)

        val recibido = intent
        val subtotal = recibido.getLongExtra("subtotal",0L)
        var domicilio :Long

        tv_valor_subtotal_detalle.text = "$".plus(subtotal)
        if(rb_recoger.isChecked){
            hideMiUbicacion()
            tv_valor_domicilio.text = "$".plus(0)
            tv_valor_total1.text = "$".plus(subtotal)
            recoger = true
        }

        rb_domicilio.setOnClickListener {
            showMiUbicacion()
            domicilio = valorDomicilio()
            tv_valor_domicilio.text = "$".plus(domicilio)
            tv_valor_total1.text = "$".plus(domicilio + subtotal)
            recoger = false
        }

        rb_recoger.setOnClickListener {
            hideMiUbicacion()
            tv_valor_domicilio.text = "$".plus(0)
            tv_valor_total1.text = "$".plus(subtotal)
            recoger = true
        }

        bt_finalizar.setOnClickListener {
            val intent = Intent(this,FinalActivity::class.java)
            intent.putExtra("recoger",recoger)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


    }

    private fun showMiUbicacion() {
        tv_mi_ubicacion.visibility = View.VISIBLE
        tv_mi_direccion.visibility = View.VISIBLE
        tv_mi_ciudad.visibility = View.VISIBLE
        divider3.visibility = View.VISIBLE
    }

    private fun hideMiUbicacion() {
        tv_mi_ubicacion.visibility = View.GONE
        tv_mi_direccion.visibility = View.GONE
        tv_mi_ciudad.visibility = View.GONE
        divider3.visibility = View.GONE
    }

    private fun valorDomicilio(): Long {
        var domicilio = 0L
        when (tv_mi_ciudad.text.toString()) {
            "Rionegro, Antioquia" -> domicilio = 3000
            "Marinilla, Antioquia" -> domicilio = 4000
            "La Ceja, Antioquia" -> domicilio = 5000
            "El Santuario, Antioquia" -> domicilio = 5000
            "El Carmen, Antioquia" -> domicilio = 5000
        }
        return domicilio
    }

    private fun cargarDireccion(email: String?) {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("usuarios")

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val usuario = datasnapshot.getValue(Usuario::class.java)
                    if (email == usuario?.correo) {
                        tv_mi_direccion.text = usuario?.direccion
                        tv_mi_ciudad.text = usuario?.ciudad.plus(", Antioquia")
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        myRef.addValueEventListener(postListener)
    }
}