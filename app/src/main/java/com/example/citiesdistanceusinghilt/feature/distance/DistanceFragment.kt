package com.example.citiesdistanceusinghilt.feature.distance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.example.citiesdistanceusinghilt.R
import com.example.citiesdistanceusinghilt.common.BaseFragment
import com.example.citiesdistanceusinghilt.data.Distance
import com.example.citiesdistanceusinghilt.data.EmptyState
import com.example.citiesdistanceusinghilt.databinding.FragmentDistanceBinding
import com.example.citiesdistance.feature.distance.DistanceViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DistanceFragment : BaseFragment() {
    private var _binding: FragmentDistanceBinding? = null
    private val binding get() = _binding!!
    private val distanceViewModel: DistanceViewModel by viewModel()
    private var adapter: DistanceAdapter = DistanceAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDistanceBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareSwipeRefreshLayout()
        prepareRecyclerViewItemsListener()
        prepareSwipeRefreshLayoutListener()
        prepareDistanceViewModel()
    }

    private fun prepareDistanceViewModel() {
        distanceViewModel.distanceLiveData.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it as ArrayList<Distance>)
                adapter.submitList(it)
                binding.recyclerViewDistance.adapter = adapter
            }
        }

        distanceViewModel.progressDialogLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        distanceViewModel.snackBarLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                showSnackBar(message)
            }
        }

        distanceViewModel.emptyStateLiveData.observe(viewLifecycleOwner) {
            val emptyState = showEmptyState(R.layout.view_distance_empty_state)
            if (it.mustShow) {
                prepareEmptyState(emptyState, it)
            } else
                emptyState?.findViewById<ConstraintLayout>(R.id.constraintLayout_viewDistanceEmptyState_rootView)
                    ?.visibility = View.GONE
        }
    }

    private fun prepareEmptyState(emptyState: View?, it: EmptyState) {
        emptyState?.let { view ->
            view.findViewById<TextView>(R.id.textView_viewDistanceEmptyState).text =
                getString(it.messageResId)
            view.findViewById<Button>(R.id.button_viewDistanceEmptyState_backToHome).visibility =
                if (it.mustShowCallToActionButton) View.VISIBLE else View.GONE
            view.findViewById<Button>(R.id.button_viewDistanceEmptyState_backToHome)
                .setOnClickListener {
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.home)
                }
        }
    }

    private fun prepareSwipeRefreshLayoutListener() {
        binding.swipeRefreshLayoutDistance.setOnRefreshListener {
            distanceViewModel.refreshList()
            binding.swipeRefreshLayoutDistance.isRefreshing = false
        }
    }

    private fun prepareRecyclerViewItemsListener() {
        adapter.onLongClick = {
            distanceViewModel.deleteDistance(it)
        }
    }

    private fun prepareSwipeRefreshLayout() {
        binding.swipeRefreshLayoutDistance.setColorSchemeResources(R.color.colorPrimary)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}