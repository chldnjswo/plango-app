package com.plango.app.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.plango.app.databinding.FragmentHomeStep3Binding


class HomeStep3 : Fragment() {
    private var _binding: FragmentHomeStep3Binding? = null
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentHomeStep3Binding.inflate(inflater,container,false)
        return binding.root
    }


}