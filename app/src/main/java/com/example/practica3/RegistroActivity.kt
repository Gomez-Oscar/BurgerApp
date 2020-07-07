package com.example.practica3
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_registro.*
import java.text.SimpleDateFormat
import java.util.*

class RegistroActivity : AppCompatActivity() {

    private var fecha = ""
    private var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val format = "MM/dd/yyyy"
                val simpleDateFormat = SimpleDateFormat(format, Locale.US)
                fecha = simpleDateFormat.format(cal.time).toString()
                tv_fecha_nacimiento.text = fecha
            }

        ibt_calendario.setOnClickListener {

            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        bt_guardar.setOnClickListener {

            val nombre = et_nombre.text.toString()
            val correo = et_correo.text.toString()
            val telefono = et_telefono.text.toString()
            val contrasena = et_contrasena.text.toString()
            val contrasenax2 = et_contrasenax2.text.toString()
            val genero = if (rb_masculino.isChecked) "Masculino" else "Femenino"
            var hobbies = ""
            val ciudad = sp_ciudad.selectedItem.toString()

            if (cb_hobbie1.isChecked) hobbies = "$hobbies Montar en bicicleta"
            if (cb_hobbie2.isChecked) hobbies = "$hobbies Jugar fútbol"
            if (cb_hobbie3.isChecked) hobbies = "$hobbies Tocar guitarra"
            if (cb_hobbie4.isChecked) hobbies = "$hobbies Practicar bungee jumping"


            if (contrasena == contrasenax2) {
                if (nombre.isEmpty() || nombre.isBlank()) {
                    Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show()
                } else if (correo.isEmpty() || correo.isBlank()) {
                    Toast.makeText(this, "Ingrese un correo electrónico", Toast.LENGTH_SHORT).show()
                } else if (telefono.isEmpty() || telefono.isBlank()) {
                    Toast.makeText(this, "Ingrese un teléfono", Toast.LENGTH_SHORT).show()
                } else if (contrasena.isEmpty() || contrasena.isBlank()) {
                    Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show()
                } else if (contrasena.length < 6) {
                    Toast.makeText(
                        this,
                        "La contraseña debe tener mínimo 6 dígitos",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (ciudad.isEmpty()) {
                    Toast.makeText(this, "Ingrese una ciudad de nacimiento", Toast.LENGTH_SHORT)
                        .show()
                } else if (fecha.isEmpty()) {
                    Toast.makeText(this, "Ingrese una fecha de nacimiento", Toast.LENGTH_SHORT)
                        .show()
                } else {

                    val intent = Intent()
                    intent.putExtra("correo", et_correo.text.toString())
                    intent.putExtra("contraseña", et_contrasena.text.toString())
                    setResult(Activity.RESULT_OK,intent)
                    finish()
                }

            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }

        }

    }

}