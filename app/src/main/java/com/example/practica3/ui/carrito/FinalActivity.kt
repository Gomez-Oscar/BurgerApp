package com.example.practica3.ui.carrito

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.practica3.MainActivity
import com.example.practica3.R
import kotlinx.android.synthetic.main.activity_final.*

class FinalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)

        val recibido = intent.extras
        val recoger = recibido?.getBoolean("recoger")

        if(recoger!!){
            tv_recoger.visibility = View.VISIBLE
            tv_domicilio.visibility = View.GONE
        }else {
            tv_recoger.visibility = View.GONE
            tv_domicilio.visibility = View.VISIBLE
        }

        bt_continuar.setOnClickListener {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }

    override fun onBackPressed() {}
}