package com.example.proyecto2t_pmdm.clases

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto2t_pmdm.R
import com.example.proyecto2t_pmdm.databinding.LayoutItemBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore

class ItemAdapter(private val context: Context, private var items:List<Item>):RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    //Creamos el ViewHolder
    class ItemViewHolder(private val binding: LayoutItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data:Item){
            //Acceder a las vistas directamente a través del binding
            binding.nombreRv.text = data.nombre
            binding.estadoRv.text = data.estado
            binding.disponibilidadRv.text = data.disponibilidad

            //Para las fotos del recycler activity
//            Glide
//                .with(binding.root)
//                .load(data.foto)
//                .into(binding.fotoRv)

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
                val db = com.google.firebase.ktx.Firebase.firestore

                db.collection("amigos")
                    .document(data.nombre)
                    .update("fav", data.fav)
            }
        }
    }

    //Inflamos el layout para cada item, es decir, cargamos la vista gráfica de las cuadrículas de la vista
    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ItemViewHolder {
        val binding = LayoutItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(binding)
    }

    //Devuelve el tamaño de la lista
    override fun getItemCount(): Int {
        return items.size
    }

    //Asignamos los valores de los datos a cada vista
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateList(newList: List<Item>){
        items = newList
        notifyDataSetChanged()
    }

}