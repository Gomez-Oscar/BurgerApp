package com.example.practica3.ui.list.compras

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.R
import com.example.practica3.ui.model.Compras
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.compras_list.*

class ListCompras : Fragment() {

    private var comprasList: MutableList<Compras> = mutableListOf()
    lateinit var comprasRVAdapter: ComprasRVAdapter

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

    }

    private fun cargarCompras() {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("compras")

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                comprasList.clear()
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val compra = datasnapshot.getValue(Compras::class.java)
                    comprasList.add(compra!!)
                }
                comprasRVAdapter.notifyDataSetChanged()

                /*if(!snapshot.exists()){
                    linear.visibility = View.INVISIBLE
                    tv_carrito_vacio.visibility = View.VISIBLE
                }*/

            }

            override fun onCancelled(error: DatabaseError) {}
        }
        myRef.addValueEventListener(postListener)

    }

    private fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}