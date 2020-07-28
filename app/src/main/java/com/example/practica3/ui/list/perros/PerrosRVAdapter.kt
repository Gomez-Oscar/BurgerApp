package com.example.practica3.ui.list.perros

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.R
import com.example.practica3.ui.model.Compras
import com.example.practica3.ui.model.Perros
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_producto.view.*

class PerrosRVAdapter(
    var perrosList: ArrayList<Perros>
) : RecyclerView.Adapter<PerrosRVAdapter.PerrosViewHolder>() {

    var idCompra: String? = ""
    var cant = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PerrosViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return PerrosViewHolder(itemView)
    }

    override fun getItemCount(): Int = perrosList.size

    override fun onBindViewHolder(
        holder: PerrosViewHolder,
        position: Int
    ) {
        val perro: Perros = perrosList[position]
        holder.bindPerro(perro)
        holder.itemView.bt_anadir.setOnClickListener {
            //Log.d("bien", hamburguesa.nombre)
            crearCompraEnBaseDeDatos(perro.nombre, perro.precio, perro.url)
        }

    }

    class PerrosViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bindPerro(perro: Perros) {
            itemView.tv_nombre.text = perro.nombre
            itemView.tv_descripcion.text = perro.descripcion
            itemView.tv_precio.text = "$".plus(perro.precio.toString())
            Picasso.get().load(perro.url).into(itemView.iv_producto)
        }
    }

    private fun crearCompraEnBaseDeDatos(
        nombre: String,
        precio: Long,
        url: String
    ) {

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("compras")
        var compraExiste = false

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                //Log.d("data",snapshot.toString())
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val compra = datasnapshot.getValue(Compras::class.java)
                    if (compra?.nombre == nombre) {
                        compraExiste = true
                        idCompra = compra.id
                        cant = compra.cantidad
                    }
                }
                if (!compraExiste) {
                    val id = myRef.push().key

                    val compra = Compras(
                        id,
                        nombre,
                        precio,
                        precio,
                        url,
                        1
                    )
                    if (id != null) {
                        myRef.child(id).setValue(compra)
                    }
                } else {
                    val childUpdate = HashMap<String, Any>()
                    childUpdate["cantidad"] = cant + 1
                    childUpdate["precio"] = precio * (cant + 1)

                    myRef.child(idCompra!!).updateChildren(childUpdate)
                }
            }
        }
        myRef.addListenerForSingleValueEvent(postListener)
    }


}