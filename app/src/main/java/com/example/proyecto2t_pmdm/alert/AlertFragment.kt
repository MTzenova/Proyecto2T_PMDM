package com.example.proyecto2t_pmdm.alert

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.proyecto2t_pmdm.databinding.FragmentAlertBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AlertFragment : DialogFragment() {
    private lateinit var binding: FragmentAlertBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val builder = AlertDialog.Builder(requireActivity())
        binding = FragmentAlertBinding.inflate(layoutInflater)

        binding.buttonSubmit.setOnClickListener {

            //Actualizar BBDD
            val db = Firebase.firestore

            val amigo = hashMapOf(
                "id" to binding.editTextId.text.toString(),
                "nombre" to binding.editTextName.text.toString(),
                "fav" to false
            )

            db.collection("amigos")
                .document(binding.editTextId.text.toString())
                .set(amigo)
                .addOnSuccessListener { Log.d("AlertFragment", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w("AlertFragment", "Error writing document", e) }

            // Procesar los valores obtenidos
            dismiss() // Cierra el diálogo después de enviar

        }

        val dialog = builder.setView(binding.root)
            .setTitle("Nuevo Amigo")
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }
            .create()

        dialog.setOnShowListener {
            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )

        }

        return dialog

    }


}