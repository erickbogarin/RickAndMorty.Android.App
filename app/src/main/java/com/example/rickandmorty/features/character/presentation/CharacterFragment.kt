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
        setupObservers()
        fetchCharacters()
    }

    private fun fetchCharacters() {
        vm.getCharacters()
    }

    private fun setupRecyclerView() {
        characterAdapter = CharacterAdapter(
            context = requireContext(),
            onFavoriteClick = { character ->
                vm.toggleFavorite(character) // Atualiza o estado de favorito na ViewModel
            },
            isFavorite = { character ->
                vm.isFavorite(character) // Verifica se o personagem é favorito
            }
        )
        binding.characterList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = characterAdapter
        }
    }

    private fun setupObservers() {
        vm.characters.observe(viewLifecycleOwner, SafeObserver { items ->
            characterAdapter.removeLoadingView()
            characterAdapter.addItems(items.map { ListItem.CharacterItem(it) })
        })

        vm.favoriteStatus.observe(viewLifecycleOwner) { (character, isFavorite) ->
            characterAdapter.notifyDataSetChanged() // Atualiza a lista quando o estado de favorito muda
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = CharacterFragment()
    }
}