package com.example.practica3.ui.carrito

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practica3.R
import com.example.practica3.ui.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.compras_list.*
import kotlinx.android.synthetic.main.fragment_detalles.*

class DetallesFragment : Fragment() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detalles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = mAuth.currentUser
        cargarDireccion(user?.email)

        /*val tvSubtotal = tv_valor_subtotal.text.toString()
        val valSubtotal = ""

        for(letter in tvSubtotal){
            if(letter != '$'){
                valSubtotal.plus(letter.toString())
            }
        }
        Log.d("subtotal valor",valSubtotal)*/

        val subtotal = 10000L
        var domicilio = valorDomicilio()

        tv_valor_subtotal_detalle.text = "$".plus(subtotal)

        if(rb_recoger.isChecked){
            hideMiUbicacion()
            tv_valor_domicilio.text = "$".plus(0)
            tv_valor_total1.text = "$".plus(subtotal)
        }

        rb_domicilio.setOnClickListener {
            showMiUbicacion()
            domicilio = valorDomicilio()
            tv_valor_domicilio.text = "$".plus(domicilio)
            tv_valor_total1.text = "$".plus(domicilio + subtotal)
        }

        rb_recoger.setOnClickListener {
            hideMiUbicacion()
            tv_valor_domicilio.text = "$".plus(0)
            tv_valor_total1.text = "$".plus(subtotal)
        }

        /* bt_finalizar.setOnClickListener {
             findNavController().navigate(R.id.action_DetallesFragment_to_PedidoRealizadoFragment)

             /*val userMail = mAuth.currentUser?.email
             val mailIntent = Intent(Intent.ACTION_VIEW)
             val data = Uri.parse("mailto:? subject = subject text &body = body text &to=$userMail")
             mailIntent.data = data
             startActivity(Intent.createChooser(mailIntent, "Send mail..."))*/
         }

         bt_cambiar_dir.setOnClickListener {
             findNavController().navigate(R.id.action_DetallesFragment_to_DireccionFragment)
         }*/
    }

    private fun showMiUbicacion() {
        tv_mi_ubicacion.visibility = View.VISIBLE
        tv_mi_direccion.visibility = View.VISIBLE
        tv_mi_ciudad.visibility = View.VISIBLE
        divider3.visibility = View.VISIBLE
    }

    private fun hideMiUbicacion() {
        tv_mi_ubicacion.visibility = View.GONE
        tv_mi_direccion.visibility = View.GONE
        tv_mi_ciudad.visibility = View.GONE
        divider3.visibility = View.GONE
    }

    private fun valorDomicilio(): Long {
        var domicilio = 0L
        when (tv_mi_ciudad.text.toString()) {
            "Rionegro, Antioquia" -> domicilio = 3000
            "Marinilla, Antioquia" -> domicilio = 4000
            "La Ceja, Antioquia" -> domicilio = 5000
            "El Santuario, Antioquia" -> domicilio = 5000
            "El Carmen, Antioquia" -> domicilio = 5000
        }
        return domicilio
    }

    private fun cargarDireccion(email: String?) {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("usuarios")

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val usuario = datasnapshot.getValue(Usuario::class.java)
                    if (email == usuario?.correo) {
                        tv_mi_direccion.text = usuario?.direccion
                        tv_mi_ciudad.text = usuario?.ciudad.plus(", Antioquia")
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        myRef.addValueEventListener(postListener)
    }
}