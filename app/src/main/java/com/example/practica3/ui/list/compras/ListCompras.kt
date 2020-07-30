package com.example.practica3.ui.list.compras

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.R
import com.example.practica3.ui.carrito.DetallesActivity
import com.example.practica3.ui.model.Compras
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.compras_list.*

class ListCompras : Fragment() {

    private var comprasList: MutableList<Compras> = mutableListOf()
    lateinit var comprasRVAdapter: ComprasRVAdapter
    val database = FirebaseDatabase.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.compras_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_compras.layoutManager = LinearLayoutManager(
            activity?.applicationContext,
            RecyclerView.VERTICAL,
            false
        )
        rv_compras.setHasFixedSize(true)//el tamaño de los campos es el mismo

        comprasRVAdapter =
            ComprasRVAdapter(
                comprasList as ArrayList<Compras>
            )
        rv_compras.adapter = comprasRVAdapter//se setea el adapter al recicler view

        linear.visibility = View.VISIBLE
        tv_carrito_vacio.visibility = View.GONE

        cargarCompras()
        subTotal()

        bt_continuar.setOnClickListener {
            if (tv_valor_subtotal.text.toString() != "0") {
                val subtotal = tv_valor_subtotal.text.toString().toLong()
                val intent = Intent(context, DetallesActivity::class.java)
                intent.putExtra("subtotal", subtotal)
                startActivity(intent)
            }
        }


    }

    private fun cargarCompras() {

        val myRef = database.getReference("compras")

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                comprasList.clear()
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val compra = datasnapshot.getValue(Compras::class.java)
                    comprasList.add(compra!!)
                }
                comprasRVAdapter.notifyDataSetChanged()

                /*if (!snapshot.exists()) {
                    linear.visibility = View.GONE
                    tv_carrito_vacio.visibility = View.VISIBLE
                }*/

            }

            override fun onCancelled(error: DatabaseError) {}
        }
        myRef.addValueEventListener(postListener)
    }

    private fun subTotal() {

        val myRef: DatabaseReference = database.getReference("compras")
        var acumulado = 0L

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val compra = datasnapshot.getValue(Compras::class.java)
                    acumulado += compra!!.precio
                    tv_valor_subtotal.text = "$acumulado"
                }
                Log.d("valor", "$acumulado")
            }
        }
        myRef.addListenerForSingleValueEvent(postListener)
    }

}