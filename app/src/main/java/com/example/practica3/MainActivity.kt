package com.example.practica3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.practica3.ui.home.MiCuentaActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    //estas dos variables del tipo String nullable las uso para
    // enviarlas al Login al cerrar sesión
    private var correoMain: String? = ""
    private var contrasenaMain: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val datosRecibidos = intent.extras
        correoMain = datosRecibidos?.getString("correo")
        contrasenaMain = datosRecibidos?.getString("contraseña")

        //navegación

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_menu,
                R.id.navigation_tracking,
                R.id.navigation_carrito
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_overflow, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_overflow_MainActivity) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("correo", correoMain)
            intent.putExtra("contraseña", contrasenaMain)
            startActivity(intent)
            finish()
        }

        if(item.itemId == R.id.menu_overflow_mi_cuenta){
            val intent = Intent(this, MiCuentaActivity::class.java)
            startActivity(intent)
        }

        if(item.itemId == R.id.menu_overflow_whatsapp){

            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://api.whatsapp.com/send?phone=573145004162")
            startActivity(i)
        }

        return super.onOptionsItemSelected(item)
    }
}