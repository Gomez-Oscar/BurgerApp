package com.example.practica3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    //estas dos variables reciben la info de la respuesta que me da el Registro
    private var correoGlobal: String? = ""
    private var contrasenaGlobal: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //siempre que se esté en login y se presione atrás se sale de la app

        //con esta variable recibo lo que me llegue del Main
        val datosMain = intent.extras

        bt_crear_cuenta.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivityForResult(intent, 10)
        }

        bt_iniciar_sesion.setOnClickListener {

            if (et_correo_electronico.text.toString() == correoGlobal && correoGlobal != "") {
                if (et_password.text.toString() == contrasenaGlobal && contrasenaGlobal != "") {

                    //lo siguiente se ejecuta al inicio cuando Registro responde a Login
                    //ya que en este caso hay datos en correoGlobal y contrasenaGlobal
                    val intent = Intent(this, MainActivity::class.java)
                    //se envía la info recibida de registro a MainActivity
                    intent.putExtra("correo", correoGlobal)
                    intent.putExtra("contraseña", contrasenaGlobal)
                    //elimino la pila para que al dar atrás en el MainActivity se salga de la app
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)


                } else {
                    Toast.makeText(
                        this,
                        "La contraseña ingresada no está registrada",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else if (et_correo_electronico.text.toString() == datosMain?.getString("correo")) {
                if (et_password.text.toString() == datosMain?.getString("contraseña")) {

                    //lo siguiente se ejecuta cuando se cierra sesión y se manda la info de Main a Login
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("correo", datosMain?.getString("correo"))
                    intent.putExtra("contraseña", datosMain?.getString("contraseña"))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        this,
                        "La contraseña ingresada no está registrada",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(
                    this,
                    "El correo ingresado no está registrado",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            correoGlobal = data?.extras?.getString("correo")
            contrasenaGlobal = data?.extras?.getString("contraseña")
        }
    }
}