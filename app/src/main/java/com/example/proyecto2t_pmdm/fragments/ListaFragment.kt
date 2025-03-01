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
import androidx.appcompat.widget.SearchView.OnQueryTextListener
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

    private fun performSearch(query: String) {
        val filteredList = amigosList.filter { item -> item.nombre.contains(query, ignoreCase = true) }
        adapter.updateList(filteredList)
    }

    private fun showRecyclerView()
    {
        adapter = ItemAdapter(mutableListOf())
        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLista.adapter = adapter

    }

    private suspend fun cargarAmigos() {
        // Cargar datos de la base de datos
        FirebaseApp.initializeApp(requireContext().applicationContext)
        val db = FirebaseFirestore.getInstance()
        amigosList.clear()

        // Ejecutar en hilo principal para evitar errores en la UI
        withContext(Dispatchers.Main) {
            // Ocultar RecyclerView y mostrar ProgressBar
            binding.rvLista.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }

        // Obtener datos desde Firestore
        try {
            val result = db.collection("amigos").get().await()
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
                amigosList.add(amigo)
            }
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error al obtener amigos: ", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        // Actualizar UI después de cargar los datos
        withContext(Dispatchers.Main) {
            binding.progressBar.visibility = ProgressBar.GONE
            binding.rvLista.visibility = View.VISIBLE

            // Actualizar el adaptador
            adapter = ItemAdapter(amigosList)
            binding.rvLista.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onResume() {
        super.onResume()

        // Ejecutar la carga de amigos en un hilo de fondo
        CoroutineScope(Dispatchers.IO).launch {
            try {
                cargarAmigos() // Llamada suspendida para cargar los amigos
            } catch (e: HttpException) {
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
        binding = FragmentListaBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvLista.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        showRecyclerView()
        // Configurar el SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    cargarAmigos() // Cargar los amigos nuevamente
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
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }

        // Botón para añadir un amigo
        binding.fab.setOnClickListener {
            val alertDialog = AlertFragment()
            alertDialog.show(requireActivity().supportFragmentManager, "FormDialogFragment")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.toolbar, menu)
        val searchItem = menu.findItem(R.id.action_search) //accedemos al elemento
        val searchView = searchItem.actionView as SearchView
        //searchView.setOnQueryTextListener(this)

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    performSearch(it) // Realiza la búsqueda final
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    performSearch(it) // Filtrar lista según la búsqueda
                }
                return true
            }
        })
    }

//    override fun onQueryTextSubmit(p0: String?): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun onQueryTextChange(p0: String?): Boolean {
//        TODO("Not yet implemented")
//    }
}
