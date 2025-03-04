package com.example.proyecto2t_pmdm.clases

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto2t_pmdm.R
import com.example.proyecto2t_pmdm.databinding.LayoutItemBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class ItemAdapter(private var items:MutableList<Item>):RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private var itemsLista:List<Item> = ArrayList(items)


    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ItemViewHolder {
        val binding = LayoutItemBinding.inflate(LayoutInflater.from(parent.context),parent,false) //inflamos el layout
        return ItemViewHolder(binding)
    }

    //vincular datos del item
    override fun onBindViewHolder(holder:ItemViewHolder, position: Int) {
        holder.bind(itemsLista[position])
    }

    //Devuelve el tamaño de la lista
    override fun getItemCount(): Int {
        return items.size
    }

    //para buscar, intento 234
//    fun searchAmigoLista(searchList:List<Item>){
//        items.clear()
//        items.addAll(searchList)
//        notifyDataSetChanged()
//    }

    //para buscar lo que se pasa por parametro
    fun filtrar(query: String) {
        items = if (query.isEmpty()) {
            ArrayList(itemsLista)
        } else { //si está vacío muestra ttodo
            itemsLista.filter { it.nombre.contains(query, ignoreCase = true) }.toMutableList()
        }
        notifyDataSetChanged()
    }


    //Creamos el ViewHolder
    class ItemViewHolder(private val binding: LayoutItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data:Item){
            //acceder a las vistas
            binding.nombreRv.text = data.nombre
            binding.estadoRv.text = data.estado
            binding.disponibilidadRv.text = data.disponibilidad

            //para las fotos del recycler
            Glide
                .with(binding.root.context)
                .load(data.foto)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.fotoRv)

            //cambiar corazon si está en fav o no
            if(data.fav){
                binding.botonFav.setImageResource(R.drawable.fav_selected) //si es favorito
            }else{
                binding.botonFav.setImageResource(R.drawable.fav_unselected) //si no es favorito
            }

            //para cuando hacemos click
            binding.botonFav.setOnClickListener {
                if(data.fav){
                    binding.botonFav.setImageResource(R.drawable.fav_unselected)
                }else{
                    binding.botonFav.setImageResource(R.drawable.fav_selected)
                }
                data.fav = !data.fav
                //actualizar BBDD
                val db = Firebase.firestore

                db.collection("amigos")
                    .document(data.nombre)
                    .update("fav", data.fav)
                    .addOnSuccessListener {
                        val usuario = Firebase.auth.currentUser?.email.toString()
                        val usuarioDoc = db.collection("usuarios").document(usuario)
                        if(data.fav){
                            usuarioDoc.update("nFav", FieldValue.arrayUnion(data.id))
                        }else{
                            usuarioDoc.update("nFav", FieldValue.arrayRemove(data.id))
                        }
                    }
                    .addOnFailureListener {
                        exception -> exception.printStackTrace()
                    }
            }
        }
    }

    //método para actualziar la lista
    fun updateList(newList: List<Item>){
        items.clear()
        items.addAll(newList)
        itemsLista = ArrayList(newList)
        notifyDataSetChanged()
    }

}