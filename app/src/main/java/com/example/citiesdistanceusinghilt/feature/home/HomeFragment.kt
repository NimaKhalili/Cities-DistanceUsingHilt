package com.example.citiesdistanceusinghilt.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.example.citiesdistanceusinghilt.R
import com.example.citiesdistanceusinghilt.common.BaseFragment
import com.example.citiesdistanceusinghilt.databinding.FragmentHomeBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {
    private val homeViewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var citiesArrayList: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        citiesArrayList = ArrayList<String>(listOf(*resources.getStringArray(R.array.citiesList)))

        prepareMainViewModel()
        prepareTextInputLayoutMainBeginning()
        prepareTextInputLayoutMainDestination()
        prepareButtonMainDistanceCalculateOnClick()
    }

    private fun prepareButtonMainDistanceCalculateOnClick() {
        binding.buttonHomeDistanceCalculate.setOnClickListener {
            val beginning = binding.textInputEditTextHomeBeginning.text.toString()
            val destination = binding.textInputEditTextHomeDestination.text.toString()
            if (beginning.isNotEmpty() && destination.isNotEmpty()) {
                getDistanceCalculate(beginning, destination)
            }else{
                showToast("لطفا هر دو گزینه را پر کنید")
            }
        }
    }

    private fun getDistanceCalculate(beginning: String, destination: String) {
        if (beginning != destination) {
            homeViewModel.getDistance(beginning, destination)
        }
        else
            showToast("امکان محاسبه دو شهر مشابه وجود ندارد")
    }

    private fun prepareTextInputLayoutMainDestination() {
        binding.textInputLayoutHomeDestination.setStartIconOnClickListener {
            preparePopupMenu(
                binding.textInputLayoutHomeDestination,
                binding.textInputEditTextHomeDestination
            )
        }
    }

    private fun prepareTextInputLayoutMainBeginning() {
        binding.textInputLayoutHomeBeginning.setStartIconOnClickListener {
            preparePopupMenu(
                binding.textInputLayoutHomeBeginning,
                binding.textInputEditTextHomeBeginning
            )
        }
    }

    private fun preparePopupMenu(
        textInputLayout: TextInputLayout,
        textInputEditText: TextInputEditText
    ) {
        val popupMenu = PopupMenu(context, textInputLayout)
        citiesArrayList?.forEach { city -> popupMenu.menu.add(Menu.NONE, 1, Menu.NONE, city) }
        popupMenu.setOnMenuItemClickListener { item ->
            textInputEditText.setText(item.toString())
            true
        }
        popupMenu.show()
    }

    private fun prepareMainViewModel() {
        homeViewModel.distanceLiveData.observe(viewLifecycleOwner) {
            binding.textViewHomeDistanceShow.text = "$it کیلومتر"
        }

        homeViewModel.progressDialogLiveData.observe(viewLifecycleOwner) {
            if (it)
                binding.progressBarHomeDistanceShow.visibility = View.VISIBLE
            else
                binding.progressBarHomeDistanceShow.visibility = View.INVISIBLE
        }

        homeViewModel.snackBarLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                showSnackBar(message)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}