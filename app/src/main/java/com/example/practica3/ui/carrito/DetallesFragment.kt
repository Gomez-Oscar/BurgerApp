package com.example.practica3.ui.carrito

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.practica3.R
import kotlinx.android.synthetic.main.fragment_detalles.*

class DetallesFragment : Fragment() {

    //private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detalles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bt_finalizar.setOnClickListener {
            findNavController().navigate(R.id.action_DetallesFragment_to_PedidoRealizadoFragment)

            /*val userMail = mAuth.currentUser?.email
            val mailIntent = Intent(Intent.ACTION_VIEW)
            val data = Uri.parse("mailto:? subject = subject text &body = body text &to=$userMail")
            mailIntent.data = data
            startActivity(Intent.createChooser(mailIntent, "Send mail..."))*/


        }

        bt_cambiar_dir.setOnClickListener {
            findNavController().navigate(R.id.action_DetallesFragment_to_DireccionFragment)
        }
    }
}