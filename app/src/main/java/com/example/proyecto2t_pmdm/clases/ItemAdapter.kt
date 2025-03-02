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

    //Inflamos el layout para cada item, es decir, cargamos la vista gráfica de las cuadrículas de la vista
    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ItemViewHolder {
        val binding = LayoutItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(binding)
    }

    //Asignamos los valores de los datos a cada vista
    override fun onBindViewHolder(holder:ItemViewHolder, position: Int) {
        holder.bind(itemsLista[position])
    }

    //Devuelve el tamaño de la lista
    override fun getItemCount(): Int {
        return items.size
    }

    //para buscar
    fun filtrar(query:String){
        if(query.isEmpty()){
            items
        }else{
            items.filter{
                it.nombre.contains(query,ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }


    //Creamos el ViewHolder
    class ItemViewHolder(private val binding: LayoutItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data:Item){
            //Acceder a las vistas directamente a través del binding
            binding.nombreRv.text = data.nombre
            binding.estadoRv.text = data.estado
            binding.disponibilidadRv.text = data.disponibilidad

            //Para las fotos del recycler activity
            Glide
                .with(binding.root.context)
                .load(data.foto)
                .into(binding.fotoRv)

            //Aquí cambiamos la imagen del icono de favorito según si lo marcamos o desmarcamos
            if(data.fav){
                binding.botonFav.setImageResource(R.drawable.fav_selected) //Si es favorito
            }else{
                binding.botonFav.setImageResource(R.drawable.fav_unselected) //Si no es favorito
            }

            //Evento para cuando hacemos click
            binding.botonFav.setOnClickListener {
                if(data.fav){
                    binding.botonFav.setImageResource(R.drawable.fav_unselected)
                }else{
                    binding.botonFav.setImageResource(R.drawable.fav_selected)
                }
                data.fav = !data.fav

                //Actualizar BBDD
                val db = Firebase.firestore

                db.collection("amigos")
                    .document(data.nombre)
                    .update("fav", data.fav)
                    .addOnSuccessListener {
                        val usuario = Firebase.auth.currentUser?.email.toString()
                        val usuarioDoc = db.collection("usuarios").document(usuario)
                        if(data.fav){
                            usuarioDoc.update("nFav", FieldValue.arrayRemove(data.id))
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

    fun updateList(newList: List<Item>){
        items.clear()
        items.addAll(newList)
        itemsLista = ArrayList(newList)
        notifyDataSetChanged()
    }

}