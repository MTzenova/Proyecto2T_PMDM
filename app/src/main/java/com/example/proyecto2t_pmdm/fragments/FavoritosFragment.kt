package com.example.proyecto2t_pmdm.fragments

import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto2t_pmdm.clases.Item
import com.example.proyecto2t_pmdm.clases.ItemAdapter
import com.example.proyecto2t_pmdm.databinding.FragmentFavoritosBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class FavoritosFragment : Fragment() {
    private lateinit var binding: FragmentFavoritosBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: ItemAdapter
    private lateinit var listaFav: MutableList<Item>

    private fun showRecyclerView()
    {
        adapter = ItemAdapter(mutableListOf())
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentFavoritosBinding.inflate(inflater,container,false)
        db = FirebaseFirestore.getInstance()
        listaFav = mutableListOf()

        adapter = ItemAdapter(listaFav)
        binding.rv.adapter = adapter



        return binding.root
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.rv.visibility = View.GONE
        showRecyclerView()
        binding.swipeRefreshLayoutFav.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    loadFavorites()// Cargar fav
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error HTTP: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                } finally {
                    withContext(Dispatchers.Main) {
                        binding.swipeRefreshLayoutFav.isRefreshing = false
                    }
                }
            }
        }

//        GlobalScope.launch (Dispatchers.Main){
//            showProgressBar()
//        }

//        //Datos
//        val contactosList = listOf(
//            Item(1,R.drawable.msn_disponible,"Edu","Voy a domesticar a tu gato","(Disponible)",false),
//            Item(2,R.drawable.msn_ocupado,"David","Ocupado en este momento, no voy a responder","(Ausente)",false),
//            Item(3,R.drawable.msn_disponible,"Fuyur","Mónica, eres la mejor","(Disponible)",false),
//            Item(4,R.drawable.msn_desconectado,"Inés","Me gustan los gatos","(Desconectado)",false),
//            Item(5,R.drawable.msn_disponible_2,"Dolet","¿Alguien quiere hablar?","(Disponible)",false),
//            Item(6,R.drawable.msn_ausente,"Chaima","El chocolate con queso no está mal","(Ausente)",false),
//            Item(7,R.drawable.msn_desconectado,"Marco","Aquí está vuestro delegado, os quiero","(Desconectado)",false),
//            Item(8,R.drawable.msn_ausente,"Vero","La vida son dos días","(Ausente)",false),
//            Item(9,R.drawable.msn_desconectado,"Gabriel","No me habléis","(Desconectado)",false),
//            Item(10,R.drawable.msn_disponible,"Basty","Miauu","(Disponible)",false)
//        )
//
//        //var searchView: SearchView = view.findViewById(R.id.searchView)
//        //Referencia al RecyclerView en el layout
//        val reciclerView: RecyclerView = view.findViewById(R.id.rv)
//
//        //Configuración del RecyclerView
//        reciclerView.layoutManager = LinearLayoutManager(requireContext())
//        reciclerView.adapter = ItemAdapter(requireContext(), contactosList)
   }

    private suspend fun loadFavorites() {
        listaFav.clear()

        withContext(Dispatchers.Main) {
            binding.rv.visibility = View.GONE
        }

        try {
            val result = db.collection("amigos")
                .whereEqualTo("fav", true)
                .get().await()
            for (document in result) {
                val fav = document.get("fav") as Boolean
                val amigo = Item(
                    document.id.hashCode(), // Usando hashCode como ID no me da error
                    document.get("foto") as? Int?: 0,
                    document.get("usuario") as String,
                    document.get("estado") as? String ?: "¡Hola, estoy usando Android Messenger!",
                    document.get("disponibilidad") as? String ?: "Desconectado",
                    fav
                )
                listaFav.add(amigo)
            }

            //actualizar
            withContext(Dispatchers.Main) {
                binding.rv.visibility = View.VISIBLE
                // Actualizar el adaptador
               adapter = ItemAdapter(listaFav)
               binding.rv.adapter = adapter
                adapter.notifyDataSetChanged()

            }
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error al obtener amigos: ", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object
    {
//        //Patrón Singleton
//        private var instance: FavoritosFragment? = null
//
//        fun getInstance(): FavoritosFragment
//        {
//
//            if (instance == null)
//            {
//                instance = FavoritosFragment()
//            }
//
//            return instance!!
//        }
    }

}
