package com.example.practica3.ui.list.hamburguesas

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.R
import com.example.practica3.ui.model.Compras
import com.example.practica3.ui.model.Conteo
import com.example.practica3.ui.model.Hamburguesas
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_producto.view.*

// un viewholder es una clase que contiene los metodos que permiten
// setear la info en la interfaz de usuario

//1

class HamburguesasRVAdapter(
    private var hamburguesasList: ArrayList<Hamburguesas>
) : RecyclerView.Adapter<HamburguesasRVAdapter.HamburguesasViewHolder>() {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var idCompra: String? = ""
    var idConteo: String? = ""

    //3
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HamburguesasViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)

        return HamburguesasViewHolder(
            itemView
        )
    }

    //4
    override fun getItemCount(): Int = hamburguesasList.size

    //5
    override fun onBindViewHolder(
        holder: HamburguesasViewHolder,
        position: Int
    ) {

        val hamburguesa: Hamburguesas = hamburguesasList[position]
        holder.bindHamburguesa(hamburguesa)

        holder.itemView.bt_anadir.setOnClickListener {
            crearCompra(hamburguesa)
        }
    }

    //2
    class HamburguesasViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bindHamburguesa(hamburguesa: Hamburguesas) {
            itemView.tv_nombre.text = hamburguesa.nombre
            itemView.tv_descripcion.text = hamburguesa.descripcion
            itemView.tv_precio.text = "$".plus(hamburguesa.precio.toString())
            Picasso.get().load(hamburguesa.url).into(itemView.iv_producto)
        }

    }

    private fun crearCompra(hamburguesa: Hamburguesas) {

        val myRef: DatabaseReference = database.getReference("compras")
        var compraExiste = false
        var cant = 0

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val compra = datasnapshot.getValue(Compras::class.java)
                    if (compra?.nombre == hamburguesa.nombre) {
                        compraExiste = true
                        idCompra = compra.id
                        cant = compra.cantidad
                    }
                }
                if (!compraExiste) {
                    val id = myRef.push().key

                    val compra = Compras(
                        id,
                        hamburguesa.nombre,
                        hamburguesa.precio,
                        hamburguesa.precio,
                        hamburguesa.url,
                        1
                    )
                    myRef.child(id!!).setValue(compra)
                    sumarConteo()

                } else {
                    val childUpdate = HashMap<String, Any>()
                    childUpdate["cantidad"] = cant + 1
                    childUpdate["precio"] = hamburguesa.precio * (cant + 1)
                    myRef.child(idCompra!!).updateChildren(childUpdate)
                }
            }
        }
        myRef.addListenerForSingleValueEvent(postListener)
    }

    private fun sumarConteo() {

        val myRef: DatabaseReference = database.getReference("conteocompras")
        var compraExiste = false
        var contadorAct = 0

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val conteo = datasnapshot.getValue(Conteo::class.java)
                    if (snapshot.exists()) {
                        compraExiste = true
                        idConteo = conteo?.id
                        contadorAct = conteo?.cont!!
                    }
                }
                if (!compraExiste) {
                    val id = myRef.push().key
                    val conteo = Conteo(id, 1)
                    myRef.child(id!!).setValue(conteo)
                } else {
                    val childUpdate = HashMap<String, Any>()
                    childUpdate["cont"] = contadorAct + 1
                    myRef.child(idConteo!!).updateChildren(childUpdate)
                    Log.d("contador firebase", contadorAct.toString())
                }
            }
        }
        myRef.addListenerForSingleValueEvent(postListener)
    }

}
