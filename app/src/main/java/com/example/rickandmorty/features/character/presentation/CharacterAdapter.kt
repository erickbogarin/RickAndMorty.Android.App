package com.example.rickandmorty.features.character.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.commons.utils.pagination.PaginationAdapter
import com.example.rickandmorty.databinding.ItemCardViewBinding
import com.example.rickandmorty.databinding.ItemEndOfListBinding
import com.example.rickandmorty.databinding.ItemLoadingBinding
import com.example.rickandmorty.features.character.data.model.Character
import com.squareup.picasso.Picasso

sealed class ListItem {
    data class CharacterItem(val character: Character) : ListItem()
    object LoadingItem : ListItem()
    data object EndOfListItem : ListItem()
}

class CharacterAdapter(
    private val context: Context,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), PaginationAdapter {

    private val itemSet: MutableSet<ListItem> = mutableSetOf()
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val VIEW_TYPE_END_OF_LIST = 2

    inner class CardViewHolder(private val binding: ItemCardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.title.text = character.name
            binding.gender.text = character.species
            binding.origin.text = character.origin.name
            Picasso.get().load(character.image).into(binding.image)
        }
    }

    inner class LoadingViewHolder(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)

    inner class EndOfListViewHolder(binding: ItemEndOfListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.endOfListContainer.setOnClickListener {
                onScrollToTopClick?.invoke()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = ItemCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CardViewHolder(binding)
        } else {
            val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CardViewHolder) {
            val characterItem = itemSet.elementAt(position) as ListItem.CharacterItem
            holder.bind(characterItem.character)
        }
    }

    override fun getItemCount(): Int {
        return itemSet.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (itemSet.elementAt(position)) {
            is ListItem.CharacterItem -> VIEW_TYPE_ITEM
            is ListItem.LoadingItem -> VIEW_TYPE_LOADING
            is ListItem.EndOfListItem -> VIEW_TYPE_END_OF_LIST
        }
    }

    fun addCharacters(newCharacters: List<Character>) {
        val startPosition = itemSet.size
        itemSet.addAll(newCharacters.map { ListItem.CharacterItem(it) })
        notifyItemRangeInserted(startPosition, newCharacters.size)
    }

    override fun addLoadingView() {
        if (itemSet.isNotEmpty() && itemSet.last() !is ListItem.LoadingItem) {
            itemSet.add(ListItem.LoadingItem)
            notifyItemInserted(itemSet.size - 1)
        }
    }

    override fun removeLoadingView() {
        if (itemSet.isNotEmpty() && itemSet.last() is ListItem.LoadingItem) {
            itemSet.remove(itemSet.last())
            notifyItemRemoved(itemSet.size)
        }
    }

    fun addEndOfListView() {
        removeLoadingView()

        if (itemSet.lastOrNull() is ListItem.EndOfListItem) {
            return
        }

        itemSet.add(ListItem.EndOfListItem)
        notifyItemInserted(itemSet.size - 1)
    }
}