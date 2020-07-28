package com.example.practica3.ui.list.bebidas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.R
import com.example.practica3.ui.model.Bebidas
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.bebidas_list.*

class ListBebidas : Fragment() {

    var bebidasList: MutableList<Bebidas> = mutableListOf()
    lateinit var bebidasRVAdapter: BebidasRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bebidas_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_bebidas.layoutManager = LinearLayoutManager(
            activity?.applicationContext,
            RecyclerView.VERTICAL,
            false
        )
        rv_bebidas.setHasFixedSize(true)

        bebidasRVAdapter =
            BebidasRVAdapter(
                bebidasList as ArrayList<Bebidas>
            )
        rv_bebidas.adapter = bebidasRVAdapter
        cargarbebidas()

    }

    private fun cargarbebidas() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("bebidas")
        //bebidasList.clear()
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    //Log.d("data", snapshot.toString())
                    val bebida = datasnapshot.getValue(Bebidas::class.java)
                    bebidasList.add(bebida!!)
                }
                bebidasRVAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        myRef.addValueEventListener(postListener)
    }

}