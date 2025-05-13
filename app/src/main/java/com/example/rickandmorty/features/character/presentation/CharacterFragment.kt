package com.example.rickandmorty.features.character.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.commons.baseui.BaseMvvmFragment
import com.example.rickandmorty.commons.baseui.SafeObserver
import com.example.rickandmorty.commons.utils.pagination.PaginationHandler
import com.example.rickandmorty.databinding.FragmentCharacterBinding

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
            onFavoriteClick = { character ->
                vm.toggleFavorite(character)
            },
            isFavorite = { character ->
                vm.isFavorite(character)
            }
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
            tag = "CharacterFragment"
        )

        paginationHandler.attach()
    }

    private fun setupObservers() {
        vm.characters.observe(viewLifecycleOwner, SafeObserver { characters ->
            characterAdapter.removeLoadingView()
            characterAdapter.addItems(characters.map { ListItem.CharacterItem(it) })
            binding.characterList.scrollToPosition(characterAdapter.itemCount - characters.size)

            if (vm.showOnlyFavorites.value == true && characters.isEmpty()) {
                binding.emptyFavoritesMessage.visibility = View.VISIBLE
            } else {
                binding.emptyFavoritesMessage.visibility = View.GONE
            }
        })

        vm.favoriteStatus.observe(viewLifecycleOwner) { (character, isFavorite) ->
            if (vm.showOnlyFavorites.value == true && !isFavorite) {
                characterAdapter.removeItem(character)
            } else {
                characterAdapter.updateCharacter(character)
            }
        }

        vm.showOnlyFavorites.observe(viewLifecycleOwner) { showOnlyFavorites ->
            characterAdapter.clear()
            binding.chipFavorites.isChecked = showOnlyFavorites
            binding.chipAll.isChecked = !showOnlyFavorites

            if (showOnlyFavorites) {
                paginationHandler.detach()
            } else {
                paginationHandler.attach()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CharacterFragment()
    }
}