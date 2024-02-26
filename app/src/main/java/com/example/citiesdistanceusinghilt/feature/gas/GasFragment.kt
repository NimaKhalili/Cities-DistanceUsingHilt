package com.example.citiesdistanceusinghilt.feature.gas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.citiesdistanceusinghilt.common.BaseFragment
import com.example.citiesdistanceusinghilt.databinding.FragmentGasBinding

class GasFragment : BaseFragment() {
    private var _binding: FragmentGasBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGasBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}