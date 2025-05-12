package com.example.rickandmorty.features.character.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.base.BaseMvvmFragment
import com.example.rickandmorty.base.SafeObserver
import com.example.rickandmorty.databinding.FragmentCharacterBinding
import kotlinx.coroutines.*

class CharacterFragment : BaseMvvmFragment() {

    private var vm by appViewModel<CharacterViewModel>()
    private lateinit var binding: FragmentCharacterBinding
    private lateinit var characterAdapter: CharacterAdapter

    private val debounceDelay = 300L
    private var debounceJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchCharacters()
        setupObservers()
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            debounceJob?.cancel()
            debounceJob = CoroutineScope(Dispatchers.Main).launch {
                delay(debounceDelay)
                checkIfLoadMoreItems(recyclerView)
            }
        }
    }

    private fun checkIfLoadMoreItems(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        if (shouldLoadMoreItems(layoutManager)) {
            loadMoreItems()
        }
    }

    private fun shouldLoadMoreItems(layoutManager: LinearLayoutManager): Boolean {
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val shouldLoad = visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                firstVisibleItemPosition >= 0
        Log.d("CharacterFragment", "shouldLoadMoreItems: $shouldLoad")
        return shouldLoad
    }


    private fun loadMoreItems() {
        characterAdapter.addLoadingView()
        vm.incrementCurrentPage()
        vm.getCharacters()
        Log.d("Character", "Loading more items for page ${vm.currentPage.value}")
    }

    private fun fetchCharacters() {
        vm.getCharacters()
    }

    private fun setupRecyclerView() {
        characterAdapter = CharacterAdapter(requireContext())
        binding.characterList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = characterAdapter
            addOnScrollListener(scrollListener)
        }
    }

    private fun setupObservers() {
        vm.characters.observe(viewLifecycleOwner, SafeObserver { items ->
            characterAdapter.removeLoadingView()
            characterAdapter.addCharacters(items)
            binding.characterList.scrollToPosition(characterAdapter.itemCount - items.size)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.characterList.removeOnScrollListener(scrollListener)
    }

    companion object {
        @JvmStatic
        fun newInstance() = CharacterFragment()
    }
}