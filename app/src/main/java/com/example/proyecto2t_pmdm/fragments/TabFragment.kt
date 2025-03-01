package com.example.proyecto2t_pmdm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyecto2t_pmdm.R
import com.example.proyecto2t_pmdm.clases.ViewPagerAdapter
import com.example.proyecto2t_pmdm.databinding.FragmentTabBinding
import com.google.android.material.tabs.TabLayoutMediator


class TabFragment : Fragment() {

    private lateinit var binding: FragmentTabBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTabBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ViewPagerAdapter(requireActivity())

        binding.viewPager.adapter = adapter


        TabLayoutMediator(binding.layoutTab, binding.viewPager)
        {
            tab, position ->
            tab.text =
                when (position)
            {
                0 -> getString(R.string.lista_amigos)
                1 -> getString(R.string.favoritos)
                else -> ""
            }

        }.attach()
    }


}