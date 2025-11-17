package com.plango.app.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.plango.app.databinding.FragmentHomeStep2Binding


class HomeStep2 : Fragment() {
    private var _binding: FragmentHomeStep2Binding? = null
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentHomeStep2Binding.inflate(inflater,container,false)
        return binding.root
    }


}