package com.example.rickandmorty.features.episodes.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.commons.baseui.BaseMvvmFragment
import com.example.rickandmorty.commons.utils.pagination.PaginationHandler
import com.example.rickandmorty.databinding.FragmentEpisodeBinding

class EpisodeFragment : BaseMvvmFragment() {
    private var vm by appViewModel<EpisodeViewModel>()
    private lateinit var binding: FragmentEpisodeBinding
    private lateinit var episodeAdapter: EpisodeAdapter
    private lateinit var paginationHandler: PaginationHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
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
            episodeAdapter.addItems(episodes.map { ListItem.EpisodeItem(it) })
            binding.recyclerViewEpisodes.scrollToPosition(episodeAdapter.itemCount - episodes.size)
        }

        vm.endOfList.observe(viewLifecycleOwner) { endOfList ->
            if (endOfList) {
                Log.d("EpisodeFragment", "End of list reached")
                episodeAdapter.addEndOfListView()
            }
        }
    }

    private fun setupRecyclerView() {
        episodeAdapter = EpisodeAdapter(requireContext()).apply {
            onScrollToTopClick = {
                binding.recyclerViewEpisodes.smoothScrollToPosition(0)
            }
        }
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
            tag = "EpisodeFragment",
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
