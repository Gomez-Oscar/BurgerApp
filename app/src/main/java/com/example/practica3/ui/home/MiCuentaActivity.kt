package com.example.practica3.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practica3.LoginActivity
import com.example.practica3.R
import com.example.practica3.ui.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_mi_cuenta.*

class MiCuentaActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_cuenta)

        val user = mAuth.currentUser

        if (user != null) {
            cargardatos(user.email)

            bt_editar.setOnClickListener {
                goToEditarActivity()
            }

        } else {
            goToLoginActivity()
        }
    }

    private fun goToEditarActivity() {
        startActivity(Intent(this, EditarActivity::class.java))
    }

    private fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun cargardatos(email: String?) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("usuarios")

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    //Log.d("data", snapshot.toString())
                    val usuario = datasnapshot.getValue(Usuario::class.java)
                    if (email == usuario?.correo) {
                        tv_nombre.text = usuario?.nombres
                        tv_apellido.text = usuario?.apellidos
                        tv_cedula.text = usuario?.cedula
                        tv_telefono.text = usuario?.telefono
                        tv_ciudad.text = usuario?.ciudad
                        tv_direccion.text = usuario?.direccion
                        tv_correo.text = usuario?.correo
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        myRef.addValueEventListener(postListener)
    }

}