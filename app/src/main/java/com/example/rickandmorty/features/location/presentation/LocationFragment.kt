package com.example.rickandmorty.features.location.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.commons.baseui.BaseMvvmFragment
import com.example.rickandmorty.commons.utils.pagination.PaginationHandler
import com.example.rickandmorty.databinding.FragmentLocationBinding

class LocationFragment : BaseMvvmFragment() {

    private var vm by appViewModel<LocationViewModel>()
    private lateinit var binding: FragmentLocationBinding
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var paginationHandler: PaginationHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLocationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupPagination()
        fetchLocations()
        setupObservers()
    }

    private fun fetchLocations() {
        vm.fetchLocations()
    }

    private fun setupRecyclerView() {
        locationAdapter = LocationAdapter(requireContext()).apply {
            onScrollToTopClick = {
                binding.locationList.smoothScrollToPosition(0) // Faz scroll para o topo
            }
        }
        binding.locationList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = locationAdapter
        }
    }

    private fun setupPagination() {
        paginationHandler = PaginationHandler(
            recyclerView = binding.locationList,
            paginationState = vm.paginationState,
            adapter = locationAdapter,
            callback = vm,
            tag = "LocationFragment",
        )
        paginationHandler.attach()
    }

    private fun setupObservers() {
        vm.locations.observe(viewLifecycleOwner) { locations ->
            locationAdapter.removeLoadingView()
            locationAdapter.addItems(locations.map { ListItem.LocationItem(it) })
            binding.locationList.scrollToPosition(locationAdapter.itemCount - locations.size)
        }

        vm.endOfList.observe(viewLifecycleOwner) { endOfList ->
            if (endOfList) {
                Log.d("LocationFragment", "End of list reached")
                locationAdapter.addEndOfListView()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        paginationHandler.detach()
    }

    companion object {
        @JvmStatic
        fun newInstance() = LocationFragment()
    }
}
