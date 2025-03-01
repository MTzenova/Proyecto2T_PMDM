package com.example.proyecto2t_pmdm.fragments

import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import com.example.proyecto2t_pmdm.R
import com.example.proyecto2t_pmdm.alert.AlertFragment
import com.example.proyecto2t_pmdm.clases.Item
import com.example.proyecto2t_pmdm.clases.ItemAdapter
import com.example.proyecto2t_pmdm.databinding.FragmentListaBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.*


class ListaFragment : Fragment() {
    private lateinit var binding:FragmentListaBinding
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private var amigosList = mutableListOf<Item>()
    private lateinit var adapter: ItemAdapter

    private fun performSearch(query:String) {
        val filteredList = amigosList.filter{item-> item.nombre.contains(query,ignoreCase = true)}
        adapter.updateList(filteredList)
    }

    private suspend fun cargarAmigos()
    {
        //cargar datos de la bbdd
        FirebaseApp.initializeApp(requireContext().applicationContext)
        val db = Firebase.firestore
        amigosList.clear()

        //Se mete este bloque para q se ejecute en el hilo princpal, si no provoca excepcion
        withContext(Dispatchers.Main) {
            //ocultar recyclerview
            binding.rvLista.visibility = View.GONE
            //leer amigos desde firebase
        binding.progressBar.visibility = View.VISIBLE
        }

        db.collection("amigos") //esto me da excepción
            .get()
            .addOnSuccessListener {
                    result ->
                for (document in result) {
                    val fav = document.get("fav") as Boolean
                    val amigo = Item(document.id.hashCode(), //con hashcode no me da error
                        document.get("usuario") as String,
                        document.get("estado") as? String?: "¡Hola, estoy usando Android Messenger!", //por defecto desconectado si no se le asigna valor
                        document.get("disponibilidad") as? String?: "Desconectado", //por defecto desconectado si no se le asigna valor
                        fav)
                    amigosList.add(amigo)
                }
            }
            .addOnFailureListener {  }

            for(i in 1..100){
            delay(50)
            binding.progressBar.progress = i

            withContext(Dispatchers.Main)
            {
                binding.progressBar.visibility = ProgressBar.GONE
                //Mostrar el RecyclerView
                binding.rvLista.visibility = View.VISIBLE
            }
        }

    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onResume()
    {
        super.onResume()

        CoroutineScope(Dispatchers.IO).launch{
            try {
                async{cargarAmigos()}.await()
            }
            catch (e: HttpException){
                //manejo de error http
                withContext(Dispatchers.Main){
                    //mostrar mensaje de error
                    Toast.makeText(requireContext(),"Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }catch (e: Exception) {
                // Manejo de otros errores
                withContext(Dispatchers.Main) {
                    // Mostrar un mensaje de error al usuario
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } finally
            {
                withContext(Dispatchers.Main)
                {
                    adapter = ItemAdapter(requireContext(), amigosList)
                    binding.rvLista.adapter = adapter
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListaBinding.inflate(inflater,container,false)
        return binding.root
    }
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.swipeRefreshLayout.setOnRefreshListener {

            CoroutineScope(Dispatchers.IO).launch {
                try
                {
                    async { cargarAmigos() }.await()
                }
                catch (e: HttpException) {
                    // Manejo del error HTTP
                    withContext(Dispatchers.Main) {
                        // Mostrar un mensaje de error al usuario
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    // Manejo de otros errores
                    withContext(Dispatchers.Main) {
                        // Mostrar un mensaje de error al usuario
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                } finally
                {
                    withContext(Dispatchers.Main)
                    {
                        adapter = ItemAdapter(requireContext(), amigosList)
                        binding.rvLista.adapter = adapter
                        adapter.notifyDataSetChanged()

                    }
                }

            }

            binding.swipeRefreshLayout.isRefreshing = false



        }

        //boton de añadir
        binding.fab.setOnClickListener {
            val alertDialog = AlertFragment()
            alertDialog.show(requireActivity().supportFragmentManager, "FormDialogFragment")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar,menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView


//    private fun getAmigos(){
//
//    }

//        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//            override fun onQueryTextChange(newText: String?): Boolean {
//
//
//            }
//        })
    }
}