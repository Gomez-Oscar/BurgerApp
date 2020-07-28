package com.example.practica3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_password.*

class PasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        bt_enviar.setOnClickListener {

            val email = et_email.text.toString()
            if (email.isEmpty() || email.isBlank()) {
                showMessage("Ingrese un correo")
            } else {
                resatPassWord(email)
            }

        }
    }

    private fun resatPassWord(email: String) {
        val auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("es")
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showMessage("El correo fue enviado exitosamente")
                    goToLoginActivity()
                }else {
                    showMessage("Correo no valido")
                }
            }
    }

    private fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
