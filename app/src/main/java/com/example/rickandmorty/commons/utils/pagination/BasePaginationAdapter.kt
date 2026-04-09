package com.example.rickandmorty.commons.utils.pagination

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemEndOfListBinding
import com.example.rickandmorty.databinding.ItemLoadingBinding

interface SpecialListItem

object LoadingItem : SpecialListItem
object EndOfListItem : SpecialListItem

abstract class BasePaginationAdapter<T, VH : RecyclerView.ViewHolder>(
    private val viewTypeItem: Int,
    private val viewTypeLoading: Int,
    private val viewTypeEndOfList: Int,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), PaginationAdapter {

    private val itemSet: LinkedHashSet<T> = LinkedHashSet()

    var onScrollToTopClick: (() -> Unit)? = null

    abstract fun onCreateItemViewHolder(parent: ViewGroup): VH
    abstract fun onBindItemViewHolder(holder: VH, item: T)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            viewTypeItem -> onCreateItemViewHolder(parent)
            viewTypeLoading -> createLoadingViewHolder(parent)
            viewTypeEndOfList -> createEndOfListViewHolder(parent)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == viewTypeItem) {
            @Suppress("UNCHECKED_CAST")
            onBindItemViewHolder(holder as VH, itemSet.elementAt(position))
        }
    }

    override fun getItemCount(): Int = itemSet.size

    fun getCurrentItems(): List<T> = itemSet.toList()

    override fun getItemViewType(position: Int): Int {
        return when (itemSet.elementAt(position)) {
            is LoadingItem -> viewTypeLoading
            is EndOfListItem -> viewTypeEndOfList
            else -> viewTypeItem
        }
    }

    fun addItems(newItems: List<T>) {
        removeSpecialItems()
        val startPosition = itemSet.size
        val filteredItems = newItems.filter { it !in itemSet }
        itemSet.addAll(filteredItems)
        if (filteredItems.isNotEmpty()) {
            notifyItemRangeInserted(startPosition, filteredItems.size)
        }
    }

    override fun addLoadingView() {
        removeEndOfListView()
        if (itemSet.isNotEmpty() && itemSet.last() !is LoadingItem) {
            @Suppress("UNCHECKED_CAST")
            itemSet.add(LoadingItem as T)
            notifyItemInserted(itemSet.size - 1)
        }
    }

    override fun removeLoadingView() {
        if (itemSet.isNotEmpty() && itemSet.last() is LoadingItem) {
            val removedIndex = itemSet.size - 1
            itemSet.remove(itemSet.last())
            notifyItemRemoved(removedIndex)
        }
    }

    fun addEndOfListView() {
        removeLoadingView()
        if (itemSet.lastOrNull() !is EndOfListItem) {
            @Suppress("UNCHECKED_CAST")
            itemSet.add(EndOfListItem as T)
            notifyItemInserted(itemSet.size - 1)
        }
    }

    fun clearItems() {
        itemSet.clear()
        notifyDataSetChanged()
    }

    fun removeItemAt(index: Int) {
        if (index in 0 until itemSet.size) {
            val item = itemSet.elementAt(index)
            itemSet.remove(item)
            notifyItemRemoved(index)
        }
    }

    private fun createLoadingViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return object : RecyclerView.ViewHolder(binding.root) {}
    }

    private fun createEndOfListViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemEndOfListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        binding.endOfListContainer.setOnClickListener {
            onScrollToTopClick?.invoke()
        }
        return object : RecyclerView.ViewHolder(binding.root) {}
    }

    private fun removeSpecialItems() {
        removeEndOfListView()
        removeLoadingView()
    }

    private fun removeEndOfListView() {
        if (itemSet.isNotEmpty() && itemSet.last() is EndOfListItem) {
            val removedIndex = itemSet.size - 1
            itemSet.remove(itemSet.last())
            notifyItemRemoved(removedIndex)
        }
    }
}
