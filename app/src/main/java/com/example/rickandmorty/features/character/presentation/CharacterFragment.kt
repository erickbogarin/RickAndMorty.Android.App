package com.example.rickandmorty.features.character.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.commons.base_ui.BaseMvvmFragment
import com.example.rickandmorty.commons.base_ui.SafeObserver
import com.example.rickandmorty.commons.utils.enableScrollToTop
import com.example.rickandmorty.commons.utils.pagination.PaginationHandler
import com.example.rickandmorty.databinding.FragmentCharacterBinding
import com.example.rickandmorty.features.character.data.model.Character
import kotlinx.coroutines.*

class CharacterFragment : BaseMvvmFragment() {

    private var vm by appViewModel<CharacterViewModel>()
    private lateinit var binding: FragmentCharacterBinding
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var paginationHandler: PaginationHandler

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
        setupPagination()
        fetchCharacters()
        setupObservers()
    }

    private fun fetchCharacters() {
        vm.getCharacters()
    }

    private fun setupRecyclerView() {
        characterAdapter = CharacterAdapter(requireContext()).apply {
            onScrollToTopClick = {
                binding.characterList.smoothScrollToPosition(0) // Faz scroll para o topo
            }
        }
        binding.characterList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = characterAdapter
        }
    }

    private fun setupPagination() {
        paginationHandler = PaginationHandler(
            recyclerView = binding.characterList,
            paginationState = vm.paginationState,
            adapter = characterAdapter,
            callback = vm,
            tag = "CharacterFragment"
        )
        paginationHandler.attach()
    }


    private fun setupObservers() {
        vm.characters.observe(viewLifecycleOwner, SafeObserver { items ->
            characterAdapter.removeLoadingView()
            characterAdapter.addItems(items.map { ListItem.CharacterItem(it) })
            binding.characterList.scrollToPosition(characterAdapter.itemCount - items.size)
        })
        vm.isLastPage.observe(viewLifecycleOwner) { isLastPage ->
            if (isLastPage) {
                Log.d("CharacterFragment", "End of list reached")
            }
        }
        vm.endOfList.observe(viewLifecycleOwner) { endOfList ->
            if (endOfList) {
                Log.d("CharacterFragment", "End of list reached")
                characterAdapter.addEndOfListView()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        paginationHandler.detach()
    }

    companion object {
        @JvmStatic
        fun newInstance() = CharacterFragment()
    }
}