package com.example.practica3.ui.list.complementos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.R
import com.example.practica3.ui.model.Complementos
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.complementos_list.*

class ListComplementos : Fragment() {

    var complementosList: MutableList<Complementos> = mutableListOf()
    lateinit var complementosRVAdapter: ComplementosRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.complementos_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_complementos.layoutManager = LinearLayoutManager(
            activity?.applicationContext,
            RecyclerView.VERTICAL,
            false
        )
        rv_complementos.setHasFixedSize(true)

        complementosRVAdapter =
            ComplementosRVAdapter(
                complementosList as ArrayList<Complementos>
            )
        rv_complementos.adapter = complementosRVAdapter
        cargarcomplementos()

    }

    private fun cargarcomplementos() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("complementos")
        //complementosList.clear()
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    //Log.d("data", snapshot.toString())
                    val complemento = datasnapshot.getValue(Complementos::class.java)
                    complementosList.add(complemento!!)
                }
                complementosRVAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        myRef.addValueEventListener(postListener)
    }

}