package com.example.practica3.ui.list.complementos

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.R
import com.example.practica3.ui.model.Complementos
import com.example.practica3.ui.model.Compras
import com.example.practica3.ui.model.Conteo
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_producto.view.*

class ComplementosRVAdapter(
    var complementosList: ArrayList<Complementos>
) : RecyclerView.Adapter<ComplementosRVAdapter.ComplementosViewHolder>() {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var idCompra: String? = ""
    var idConteo: String? = ""
    var cant = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ComplementosViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ComplementosViewHolder(itemView)
    }

    override fun getItemCount(): Int = complementosList.size

    override fun onBindViewHolder(
        holder: ComplementosViewHolder,
        position: Int
    ) {
        val complemento: Complementos = complementosList[position]
        holder.bindComplemento(complemento)
        holder.itemView.bt_anadir.setOnClickListener {
            crearCompra(complemento)
        }
    }

    class ComplementosViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bindComplemento(complemento: Complementos) {
            itemView.tv_nombre.text = complemento.nombre
            itemView.tv_descripcion.text = complemento.descripcion
            itemView.tv_precio.text = "$".plus(complemento.precio.toString())
            Picasso.get().load(complemento.url).into(itemView.iv_producto)
        }
    }

    private fun crearCompra(complemento: Complementos) {

        val myRef: DatabaseReference = database.getReference("compras")
        var compraExiste = false

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val compra = datasnapshot.getValue(Compras::class.java)
                    if (compra?.nombre == complemento.nombre) {
                        compraExiste = true
                        idCompra = compra.id
                        cant = compra.cantidad
                    }
                }
                if (!compraExiste) {
                    val id = myRef.push().key

                    val compra = Compras(
                        id,
                        complemento.nombre,
                        complemento.precio,
                        complemento.precio,
                        complemento.url,
                        1
                    )
                    myRef.child(id!!).setValue(compra)
                    sumarConteo()

                } else {
                    val childUpdate = HashMap<String, Any>()
                    childUpdate["cantidad"] = cant + 1
                    childUpdate["precio"] = complemento.precio * (cant + 1)
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