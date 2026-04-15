package com.example.rickandmorty.features.character.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.commons.baseui.BaseMvvmFragment
import com.example.rickandmorty.commons.utils.pagination.PaginationHandler
import com.example.rickandmorty.databinding.FragmentCharacterBinding

class CharacterFragment : BaseMvvmFragment() {

    private var vm by appViewModel<CharacterViewModel>()
    private lateinit var binding: FragmentCharacterBinding
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var paginationHandler: PaginationHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCharacterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupPagination()
        setupFilterChips()
        setupObservers()
        fetchCharacters()
    }

    private fun fetchCharacters() {
        vm.onLoadMore()
    }

    private fun setupRecyclerView() {
        characterAdapter = CharacterAdapter(
            context = requireContext(),
            onFavoriteClick = { model ->
                vm.toggleFavorite(model.character)
            },
        )

        binding.characterList.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupFilterChips() {
        binding.chipAll.setOnClickListener {
            vm.setShowOnlyFavorites(false)
        }

        binding.chipFavorites.setOnClickListener {
            vm.setShowOnlyFavorites(true)
        }

        binding.chipAll.isChecked = true
    }

    private fun setupPagination() {
        paginationHandler = PaginationHandler(
            recyclerView = binding.characterList,
            paginationState = vm.paginationState,
            adapter = characterAdapter,
            callback = vm,
            tag = "CharacterFragment",
        )

        paginationHandler.attach()
    }

    private fun setupObservers() {
        vm.paginationState.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (!isLoading) {
                characterAdapter.removeLoadingView()
            }
        }

        vm.uiState.observe(viewLifecycleOwner) { uiState ->
            characterAdapter.removeLoadingView()
            characterAdapter.submitItems(uiState.items.map { ListItem.CharacterItem(it) })
            binding.chipFavorites.isChecked = uiState.showOnlyFavorites
            binding.chipAll.isChecked = !uiState.showOnlyFavorites
            binding.emptyFavoritesMessage.visibility =
                if (uiState.showEmptyFavoritesMessage) View.VISIBLE else View.GONE

            if (uiState.showOnlyFavorites) {
                paginationHandler.detach()
            } else {
                paginationHandler.attach()
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
