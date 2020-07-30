package com.example.practica3.ui.list.compras

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.R
import com.example.practica3.ui.model.Compras
import com.example.practica3.ui.model.Conteo
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.compras_list.*
import kotlinx.android.synthetic.main.compras_list.view.*
import kotlinx.android.synthetic.main.compras_list.view.tv_valor_subtotal
import kotlinx.android.synthetic.main.item_carrito.view.*
import kotlinx.android.synthetic.main.item_producto.view.iv_producto
import kotlinx.android.synthetic.main.item_producto.view.tv_nombre
import kotlinx.android.synthetic.main.item_producto.view.tv_precio

class ComprasRVAdapter(
    private var ComprasList: ArrayList<Compras>
) : RecyclerView.Adapter<ComprasRVAdapter.ComprasViewHolder>() {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference("compras")
    var idConteo: String? = ""

    //3
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ComprasViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_carrito, parent, false)
        return ComprasViewHolder(
            itemView
        )
    }

    //4
    override fun getItemCount(): Int = ComprasList.size

    //5
    override fun onBindViewHolder(
        holder: ComprasViewHolder,
        position: Int
    ) {
        val compra: Compras = ComprasList[position]
        holder.bindCompra(compra)

        holder.itemView.iv_mas.setOnClickListener {
            sumarCompra(compra)
        }

        holder.itemView.iv_menos.setOnClickListener {
            restarCompra(compra)
        }

        holder.itemView.iv_eliminar.setOnClickListener {
            eliminarCompra(compra.id)
            restarConteo()
        }




    }

    //2
    class ComprasViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bindCompra(compra: Compras) {
            itemView.tv_nombre.text = compra.nombre
            itemView.tv_precio.text = "$".plus(compra.precio.toString())
            Picasso.get().load(compra.url).into(itemView.iv_producto)
            itemView.tv_numero.text = compra.cantidad.toString()
        }

    }

    private fun eliminarCompra(id: String?) {
        myRef.child(id!!).removeValue()
    }

    private fun restarCompra(compra: Compras) {

        val childUpdate = HashMap<String, Any>()
        if (compra.cantidad != 1) {
            childUpdate["cantidad"] = compra.cantidad - 1
            childUpdate["precio"] = compra.precio - compra.preciounitario
            myRef.child(compra.id!!).updateChildren(childUpdate)
        }
    }

    private fun sumarCompra(compra: Compras) {

        val childUpdate = HashMap<String, Any>()
        childUpdate["cantidad"] = compra.cantidad + 1
        childUpdate["precio"] = compra.precio + compra.preciounitario
        myRef.child(compra.id!!).updateChildren(childUpdate)
    }

    private fun restarConteo() {

        val myRef: DatabaseReference = database.getReference("conteocompras")
        var compraExiste = false
        var contadorAct = 0
        val postListener2 = object : ValueEventListener {
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
                    val conteo = Conteo(
                        id,
                        1
                    )
                    myRef.child(id!!).setValue(conteo)
                } else {
                    val childUpdate = HashMap<String, Any>()

                    childUpdate["cont"] = contadorAct - 1
                    myRef.child(idConteo!!).updateChildren(childUpdate)
                    Log.d("contador firebase", contadorAct.toString())

                }
            }
        }
        myRef.addListenerForSingleValueEvent(postListener2)
    }

}
