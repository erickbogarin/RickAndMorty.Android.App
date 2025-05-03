package com.example.rickandmorty.features.character.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.commons.utils.pagination.BasePaginationAdapter
import com.example.rickandmorty.databinding.ItemCardViewBinding
import com.example.rickandmorty.databinding.ItemEndOfListBinding
import com.example.rickandmorty.databinding.ItemLoadingBinding
import com.example.rickandmorty.features.character.data.model.Character
import com.squareup.picasso.Picasso

sealed class ListItem {
    data class CharacterItem(val character: Character) : ListItem()
}

class CharacterAdapter(
    private val context: Context
) : BasePaginationAdapter<ListItem.CharacterItem, CharacterAdapter.CardViewHolder>(
    viewTypeItem = 0,
    viewTypeLoading = 1,
    viewTypeEndOfList = 2
) {

    inner class CardViewHolder(private val binding: ItemCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.title.text = character.name
            binding.gender.text = character.species
            binding.origin.text = character.origin.name
            Picasso.get().load(character.image).into(binding.image)
        }
    }

    override fun onCreateItemViewHolder(parent: ViewGroup): CardViewHolder {
        val binding = ItemCardViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindItemViewHolder(holder: CardViewHolder, item: ListItem.CharacterItem) {
        holder.bind(item.character)
    }
}