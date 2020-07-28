package com.example.practica3.ui.list.perros

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.R
import com.example.practica3.ui.model.Perros
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.perros_list.*

class ListPerros : Fragment() {

    var perrosList: MutableList<Perros> = mutableListOf()
    lateinit var perrosRVAdapter: PerrosRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.perros_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_perros.layoutManager = LinearLayoutManager(
            activity?.applicationContext,
            RecyclerView.VERTICAL,
            false
        )
        rv_perros.setHasFixedSize(true)

        perrosRVAdapter =
            PerrosRVAdapter(
                perrosList as ArrayList<Perros>
            )
        rv_perros.adapter = perrosRVAdapter
        cargarperros()

    }

    private fun cargarperros() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("perros")
        //perrosList.clear()
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    Log.d("data", snapshot.toString())
                    val perro = datasnapshot.getValue(Perros::class.java)
                    perrosList.add(perro!!)
                }
                perrosRVAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        myRef.addValueEventListener(postListener)
    }

}