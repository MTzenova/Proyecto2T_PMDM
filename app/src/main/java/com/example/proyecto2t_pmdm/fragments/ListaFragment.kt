package com.example.proyecto2t_pmdm.fragments

import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto2t_pmdm.R
import com.example.proyecto2t_pmdm.alert.AlertFragment
import com.example.proyecto2t_pmdm.clases.Item
import com.example.proyecto2t_pmdm.clases.ItemAdapter
import com.example.proyecto2t_pmdm.databinding.FragmentListaBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class ListaFragment : Fragment(){
    private lateinit var binding: FragmentListaBinding
    private var amigosList = mutableListOf<Item>()
    private lateinit var adapter: ItemAdapter

    //método para mostrar el recycler view
    private fun showRecyclerView()
    {
        adapter = ItemAdapter(mutableListOf())
        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLista.adapter = adapter

    }

    //este si funciona para buscar
    fun searchList(text: String) {
        val filteredList = amigosList.filter { it.nombre.contains(text, ignoreCase = true) }
        adapter.updateList(filteredList) //llamamos a update para actualizar en recyclerview
    }

    private suspend fun cargarAmigos() {
        //cargamos los datos de la BBDD
        FirebaseApp.initializeApp(requireContext().applicationContext)
        val db = FirebaseFirestore.getInstance()
        amigosList.clear()

        //se tieen que ejecuta en el hilo principal para no tener errores
        withContext(Dispatchers.Main) {
            //ocultaamos recycler view
            binding.rvLista.visibility = View.GONE
            //mostramos progressbar
            binding.progressBar.visibility = View.VISIBLE
        }


        try {
            //para obtener los datos de BBDD
            val result = db.collection("amigos").get().await()
            for (document in result) {
                val fav = document.get("fav") as Boolean
                val amigo = Item(
                    document.id.hashCode(), // Usando hashCode como ID no me da error
                    document.get("foto") as? String?: "",
                    document.get("usuario") as String,
                    document.get("estado") as? String ?: "¡Hola, estoy usando Android Messenger!",
                    document.get("disponibilidad") as? String ?: "Desconectado",
                    fav
                )
                amigosList.add(amigo) //lo añadimos
            }
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error al obtener amigos: ", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        //actualizar despues de obtener datos
        withContext(Dispatchers.Main) {
            binding.progressBar.visibility = ProgressBar.GONE //desactivamos progressbar
            binding.rvLista.visibility = View.VISIBLE //activamos recyclerview

            //actualizar adapter
            adapter = ItemAdapter(amigosList)
            binding.rvLista.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onResume() {
        super.onResume()

        //usamos corrutina para llamar a cargar amigos
        CoroutineScope(Dispatchers.IO).launch {
            try {
                cargarAmigos() //llamamos a cargar amigos
            } catch (e: HttpException) { //errores
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error HTTP: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListaBinding.inflate(inflater, container, false) //inflar layour
        return binding.root
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvLista.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        showRecyclerView()
        //swipe para recargar los amigos
        binding.swipeRefreshLayout.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    cargarAmigos() //metodo de cargar amigos
                } catch (e: HttpException) { //errores
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error HTTP: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                } finally {
                    withContext(Dispatchers.Main) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }

        //boton flotante para añadir amigo a la lista
        binding.fab.setOnClickListener {
            val alertDialog = AlertFragment() //llamamos al alert fragment
            alertDialog.show(requireActivity().supportFragmentManager, "FormDialogFragment")
        }
    }

    //toolbar (no me acuerdo si este funcionaba)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.toolbar, menu)
        val searchItem = menu.findItem(R.id.action_search) //accedemos al elemento
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {adapter.filtrar(it)}
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {adapter.filtrar(it)}
                return false
            }
        })

    }

}
