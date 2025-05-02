package com.example.rickandmorty.features.episodes.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.commons.utils.pagination.PaginationAdapter
import com.example.rickandmorty.databinding.ItemEndOfListBinding
import com.example.rickandmorty.databinding.ItemEpisodeBinding
import com.example.rickandmorty.databinding.ItemLoadingBinding
import com.example.rickandmorty.features.episodes.data.model.EpisodeModel

sealed class ListItem {
    data class EpisodeItem(val episode: EpisodeModel) : ListItem()
    object LoadingItem : ListItem()
    data object EndOfListItem : ListItem()
}

var onScrollToTopClick: (() -> Unit)? = null

class EpisodeAdapter(
    private val context: Context,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), PaginationAdapter {

    private val itemSet: MutableSet<ListItem> = mutableSetOf()
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val VIEW_TYPE_END_OF_LIST = 2

    inner class CardViewHolder(private val binding: ItemEpisodeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: EpisodeModel) {
            binding.textEpisodeName.text = episode.name
            binding.textEpisodeCode.text = episode.episode
            binding.textEpisodeAirDate.text = episode.airDate
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
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CardViewHolder(binding)
            }
            VIEW_TYPE_LOADING -> {
                val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(binding)
            }
            else -> {
                val binding = ItemEndOfListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EndOfListViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemSet.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (itemSet.elementAt(position)) {
            is ListItem.EpisodeItem -> VIEW_TYPE_ITEM
            is ListItem.LoadingItem -> VIEW_TYPE_LOADING
            is ListItem.EndOfListItem -> VIEW_TYPE_END_OF_LIST
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CardViewHolder) {
            val episodeItem = itemSet.elementAt(position) as ListItem.EpisodeItem
            holder.bind(episodeItem.episode)
        }
    }

    fun addEpisodes(episodes: List<EpisodeModel>) {
        val starPosition = itemSet.size
        itemSet.addAll(episodes.map { ListItem.EpisodeItem(it) })
        notifyItemRangeInserted(starPosition, episodes.size)
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