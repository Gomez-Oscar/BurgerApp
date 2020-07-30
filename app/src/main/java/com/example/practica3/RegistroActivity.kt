package com.example.practica3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practica3.ui.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registro.*

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

        bt_guardar.setOnClickListener {

            val nombres = et_nombre.text.toString()
            val apellidos = et_apellido.text.toString()
            val cedula = et_cedula.text.toString()
            val telefono = et_telefono.text.toString()
            val ciudad = sp_ciudad.selectedItem.toString()
            val direccion = et_direccion.text.toString()
            val email = et_correo.text.toString()
            val password = et_contrasena.text.toString()
            val contrasenax2 = et_contrasenax2.text.toString()

            if (nombres.isEmpty() || nombres.isBlank()) {
                showMessage("Ingrese al menos un nombre")

            } else if (apellidos.isEmpty() || apellidos.isBlank()) {
                showMessage("Ingrese sus apellidos")

            } else if (cedula.isEmpty() || cedula.isBlank()) {
                showMessage("Ingrese su cédula")

            } else if (telefono.isEmpty() || telefono.isBlank()) {
                showMessage("Ingrese un teléfono")

            } else if (ciudad == "Ciudad") {
                showMessage("Escoja una ciudad")

            } else if (direccion.isEmpty() || direccion.isBlank()) {
                showMessage("Ingrese  una dirección")

            } else if (email.isEmpty() || email.isBlank()) {
                showMessage("Ingrese un correo electrónico")

            } else if (password.isEmpty() || password.isBlank()) {
                showMessage("Ingrese una contraseña")

            } else if (password != contrasenax2) {
                showMessage("La contraseñas NO coniciden")

            } else {

                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {
                            crearUsuarioEnBaseDeDatos(
                                nombres,
                                apellidos,
                                cedula,
                                telefono,
                                ciudad,
                                direccion,
                                email
                            )
                            onBackPressed()
                        } else {

                            val errorCode =
                                (task.exception as FirebaseAuthException?)!!.errorCode

                            when (errorCode) {
                                "ERROR_EMAIL_ALREADY_IN_USE"
                                -> showMessage("El correo ingresado ya esta registrado")

                                "ERROR_INVALID_EMAIL"
                                -> showMessage("El correo ingresado esta mal escrito")

                                "ERROR_WEAK_PASSWORD"
                                -> showMessage("La contraseña debe tener minimo 6 digitos")
                            }

                            /*Toast.makeText(
                                this, errorCode,
                                Toast.LENGTH_SHORT
                            ).show()*/
                        }
                    }

            }

        }

    }

    private fun crearUsuarioEnBaseDeDatos(
        nombres: String,
        apellidos: String,
        cedula: String,
        telefono: String,
        ciudad: String,
        direccion: String,
        email: String
    ) {

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("usuarios")
        val id = myRef.push().key


        val usuario = Usuario(
            id,
            nombres,
            apellidos,
            cedula,
            telefono,
            ciudad,
            direccion,
            email
        )
        if (id != null) {
            myRef.child(id).setValue(usuario)
            showMessage("Registro Exitoso")
        }
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}
