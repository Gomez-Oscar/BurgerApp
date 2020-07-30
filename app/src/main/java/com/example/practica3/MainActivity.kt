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
import com.example.practica3.ui.model.Compras
import com.example.practica3.ui.model.Conteo
import com.example.practica3.ui.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    var idUsuarioFireBase: String? = ""
    var aux1 = false

    private lateinit var binding: ActivityMainBinding

    //val editor = SharedPreference(this)

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

        val myRef2: DatabaseReference = database.getReference("conteocompras")
        val badge = binding.navView.getOrCreateBadge(R.id.navigation_carrito)
        badge.backgroundColor = Color.RED
        badge.badgeTextColor = Color.WHITE

        var compraExiste = false
        var idConteo: String? = ""
        var contadorAct = 0

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val conteo = datasnapshot.getValue(Conteo::class.java)
                    if (snapshot.exists()) {
                        compraExiste = true
                        idConteo = conteo?.id
                        contadorAct = conteo?.cont!!
                        Log.d("contador badge cont", conteo.cont.toString())
                    }
                }
                if (!compraExiste || !aux1) {
                    badge.number = 0
                    val init = HashMap<String, Any>()
                    init["cont"] = 0
                    myRef2.child(idConteo!!).updateChildren(init)
                    //Log.d("contador badge" ,"no existe"+ badge.number.toString()!!)
                    aux1 = true
                } else {
                    badge.number = contadorAct
                    //Log.d("contador badge" , "si existe"+ contadorAct)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        myRef2.addValueEventListener(postListener)

        cargarNombre()


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

    private fun cargarNombre() {
        val email = mAuth.currentUser?.email
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
                        //editor.save("key",usuario?.id)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        myRef.addValueEventListener(postListener)
    }

    private fun limpiarCarrito() {

        val myRef2: DatabaseReference = database.getReference("compras")

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val compra = datasnapshot.getValue(Compras::class.java)
                    if (datasnapshot.exists()) {
                        myRef2.child(compra?.id!!).removeValue()
                    }
                }
            }
        }
        myRef2.addListenerForSingleValueEvent(postListener)
    }

    override fun onDestroy() {
        super.onDestroy()

        limpiarCarrito()
    }

}

/*class SharedPreference(val context: Context) {

    fun save(KEY_NAME: String, text: String?) {
        val editor: SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putString(KEY_NAME, text)
        editor.apply()
    }

    fun getValueString(KEY_NAME: String): String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_NAME, null)
    }

    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        //sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor.clear()
        editor.apply()
    }

    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.remove(KEY_NAME)
        editor.apply()
    }
}*/

