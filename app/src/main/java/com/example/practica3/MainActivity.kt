package com.example.practica3

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.practica3.databinding.ActivityMainBinding
import com.example.practica3.ui.home.MiCuentaActivity
import com.example.practica3.ui.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    var idUsuarioFireBase: String? = ""

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_menu,
                R.id.navigation_tracking,
                R.id.navigation_carrito
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        val badge = binding.navView.getOrCreateBadge(R.id.navigation_carrito)
        badge.backgroundColor = Color.RED
        badge.badgeTextColor = Color.WHITE
        badge.number = 0

        val user = mAuth.currentUser
        cargarNombre(user!!.email)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_overflow, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_overflow_log_in_out) {
            mAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        if (item.itemId == R.id.menu_overflow_mi_cuenta) {
            startActivity(Intent(this, MiCuentaActivity::class.java))
        }

        if (item.itemId == R.id.menu_overflow_whatsapp) {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://api.whatsapp.com/send?phone=573143336444")
            startActivity(i)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun conteo(): Int {

        val myRef: DatabaseReference = database.getReference("compras")
        var cont = 0

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    cont += 1
                }
                Log.d("conteo", "$cont")
            }
        }
        myRef.addListenerForSingleValueEvent(postListener)
        return cont
    }

    private fun cargarNombre(email: String?) {

        val myRef = database.getReference("usuarios")

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    //Log.d("data", snapshot.toString())
                    val usuario = datasnapshot.getValue(Usuario::class.java)
                    if (email == usuario?.correo) {
                        Toast.makeText(
                            this@MainActivity,
                            "Bienvenid@ ${usuario?.nombres}",
                            Toast.LENGTH_SHORT
                        ).show()
                        idUsuarioFireBase = usuario?.id
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        myRef.addValueEventListener(postListener)
    }


}

/*
class claseContador(cont: Int) {

    private var c = cont

    fun plus():Int{
        c += 1
        return c
    }

}*/

