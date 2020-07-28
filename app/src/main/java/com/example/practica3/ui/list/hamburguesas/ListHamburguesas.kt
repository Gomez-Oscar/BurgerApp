package com.example.practica3.ui.list.hamburguesas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.R
import com.example.practica3.ui.model.Hamburguesas
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.hamburguesas_list.*

class ListHamburguesas : Fragment() {

    var hamburguesasList: MutableList<Hamburguesas> = mutableListOf()
    lateinit var hamburguesasRVAdapter : HamburguesasRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.hamburguesas_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_hamburguesas.layoutManager = LinearLayoutManager(
            activity?.applicationContext,
            RecyclerView.VERTICAL,
            false
        )
        rv_hamburguesas.setHasFixedSize(true)//el tamaño de los campos es el mismo

        hamburguesasRVAdapter =
            HamburguesasRVAdapter(
                hamburguesasList as ArrayList<Hamburguesas>
            )
        rv_hamburguesas.adapter = hamburguesasRVAdapter//se setea el adapter al recicler view

        cargarhamburguesas()




    }

    private fun cargarhamburguesas(){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("hamburguesas")
        //hamburguesasList.clear()
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    //Log.d("data",snapshot.toString())
                    val hamburguesa = datasnapshot.getValue(Hamburguesas::class.java)
                        hamburguesasList.add(hamburguesa!!)
                }
                hamburguesasRVAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        myRef.addValueEventListener(postListener)
    }

}















