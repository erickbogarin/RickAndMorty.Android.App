package com.example.rickandmorty.features.episodes.presentation

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.commons.utils.pagination.BasePaginationAdapter
import com.example.rickandmorty.commons.utils.pagination.PaginationAdapter
import com.example.rickandmorty.databinding.ItemEndOfListBinding
import com.example.rickandmorty.databinding.ItemEpisodeBinding
import com.example.rickandmorty.databinding.ItemLoadingBinding
import com.example.rickandmorty.features.episodes.data.model.EpisodeModel

sealed class ListItem {
    data class EpisodeItem(val episode: EpisodeModel) : ListItem()
}

class EpisodeAdapter(
    private val context: Context
) : BasePaginationAdapter<ListItem.EpisodeItem, EpisodeAdapter.CardViewHolder>(
    viewTypeItem = 0,
    viewTypeLoading = 1,
    viewTypeEndOfList = 2
) {

    inner class CardViewHolder(private val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: EpisodeModel) {
            binding.textEpisodeName.text = episode.name
            binding.textEpisodeCode.text = episode.episode
            binding.textEpisodeAirDate.text = episode.airDate
        }
    }

    override fun onCreateItemViewHolder(parent: ViewGroup): CardViewHolder {
        val binding = ItemEpisodeBinding.inflate(LayoutInflater.from(context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindItemViewHolder(holder: CardViewHolder, item: ListItem.EpisodeItem) {
        holder.bind(item.episode)
    }
}