package com.example.rickandmorty.features.episodes.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.commons.base_ui.BaseMvvmFragment
import com.example.rickandmorty.commons.utils.pagination.PaginationHandler
import com.example.rickandmorty.databinding.FragmentEpisodeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EpisodeFragment : BaseMvvmFragment() {
    private var vm by appViewModel<EpisodeViewModel>()
    private lateinit var binding: FragmentEpisodeBinding
    private lateinit var episodeAdapter: EpisodeAdapter
    private lateinit var paginationHandler: PaginationHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEpisodeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupPagination()
        fetchEpisodes()
        setupObservers()
    }

    private fun fetchEpisodes() {
        vm.getEpisodes()
    }

    private fun setupObservers() {
        vm.episodes.observe(viewLifecycleOwner) { episodes ->
            episodeAdapter.removeLoadingView()
            episodeAdapter.addEpisodes(episodes)
            binding.recyclerViewEpisodes.scrollToPosition(episodeAdapter.itemCount - episodes.size)
        }

        vm.endOfList.observe(viewLifecycleOwner) { endOfList ->
            if (endOfList) {
                Log.d("EpisodeFragment", "End of list reached")
                // Show a message to the user
            }
        }
    }

    private fun setupRecyclerView() {
        episodeAdapter = EpisodeAdapter(requireContext())
        binding.recyclerViewEpisodes.apply {
            adapter = episodeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupPagination() {
        paginationHandler = PaginationHandler(
            recyclerView = binding.recyclerViewEpisodes,
            paginationState = vm.paginationState,
            adapter = episodeAdapter,
            callback = vm,
            tag = "EpisodeFragment"
        )
        paginationHandler.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        paginationHandler.detach()
    }

    companion object {
        @JvmStatic
        fun newInstance() = EpisodeFragment()
    }
}