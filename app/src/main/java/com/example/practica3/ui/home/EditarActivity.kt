package com.example.practica3.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practica3.MainActivity
import com.example.practica3.R
import com.example.practica3.ui.model.Usuario
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_editar.*

//eliminar la pila
//intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

class EditarActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var idUsuarioFireBase: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("usuarios")
        val user = mAuth.currentUser


        if (user != null) {
            cargarDatos(user.email)

            bt_guardar.setOnClickListener {

                val nombres = et_nombre.text.toString()
                val apellidos = et_apellido.text.toString()
                val cedula = et_cedula.text.toString()
                val telefono = et_telefono.text.toString()
                val direccion = et_direccion.text.toString()
                val email = et_correo.text.toString()

                val newPassword = et_nueva_contrasena.text.toString()
                val newEmail = et_correo.text.toString()
                val password = et_contrasena_ingresada.text.toString()

                if (nombres.isEmpty() || nombres.isBlank()) {
                    showMessage("Ingrese al menos un nombre")

                } else if (apellidos.isEmpty() || apellidos.isBlank()) {
                    showMessage("Ingrese sus apellidos")

                } else if (cedula.isEmpty() || cedula.isBlank()) {
                    showMessage("Ingrese sus apellidos")

                } else if (telefono.isEmpty() || telefono.isBlank()) {
                    showMessage("Ingrese un teléfono")

                } else if (direccion.isEmpty() || direccion.isBlank()) {
                    showMessage("Ingrese una dirección")

                } else if (email.isEmpty() || email.isBlank()) {
                    showMessage("Ingrese un correo electrónico")

                } else if (password.isNotEmpty() || password.isNotEmpty()) {

                    if (newPassword.isNotEmpty() || newPassword.isNotBlank()) {
                        if (newPassword.length >= 6) {
                            updatePasswordAuth(user, password, newPassword)
                        } else {
                            showMessage("La contraseña debe tener mínimo 6 dígitos")
                        }
                    }

                    if (user.email != newEmail) {
                        updateEmailAuth(user, newEmail, password)
                    }
                    updateInfoDataBase(myRef)
                    showMessage("Edición Exitosa")
                    goToMainActivity()

                } else {
                    showMessage("Ingrese su contraseña")
                }
            }

        }

    }


    private fun updatePasswordAuth(user: FirebaseUser, password: String, newPassword: String) {

        val credential = EmailAuthProvider.getCredential(user.email!!, password)

        user.reauthenticate(credential)
            .addOnCompleteListener {
                user.updatePassword(newPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showMessage("contraseña actualizada")
                        } else {
                            showMessage("contraseña no actualizada")
                        }
                    }
            }

    }

    private fun updateEmailAuth(user: FirebaseUser, newEmail: String, password: String) {

        val credential = EmailAuthProvider.getCredential(user.email!!, password)

        user.reauthenticate(credential)
            .addOnCompleteListener {
                user.updateEmail(newEmail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showMessage("email actualizado")
                        } else {
                            showMessage("email no actualizado")
                        }
                    }
            }
    }

    private fun updateInfoDataBase(myRef: DatabaseReference) {

        val childUpdate = HashMap<String, Any>()
        childUpdate["nombres"] = et_nombre.text.toString()
        childUpdate["apellidos"] = et_apellido.text.toString()
        childUpdate["cedula"] = et_cedula.text.toString()
        if (sp_ciudad.selectedItem.toString() != "Ciudad") {
            childUpdate["ciudad"] = sp_ciudad.selectedItem.toString()
        }
        childUpdate["direccion"] = et_direccion.text.toString()
        childUpdate["telefono"] = et_telefono.text.toString()
        childUpdate["correo"] = et_correo.text.toString()

        myRef.child(idUsuarioFireBase!!).updateChildren(childUpdate)
    }

    private fun cargarDatos(email: String?) {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("usuarios")

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    //Log.d("data", snapshot.toString())
                    val usuario = datasnapshot.getValue(Usuario::class.java)
                    if (email == usuario?.correo) {
                        et_nombre.setText(usuario?.nombres)
                        et_apellido.setText(usuario?.apellidos)
                        et_cedula.setText(usuario?.cedula)

                        et_correo.setText(usuario?.correo)
                        et_telefono.setText(usuario?.telefono)
                        et_direccion.setText(usuario?.direccion)
                        idUsuarioFireBase = usuario?.id
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        myRef.addValueEventListener(postListener)
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}