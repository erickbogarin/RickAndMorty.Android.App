package com.example.rickandmorty.commons.utils.pagination

import android.util.Log
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
    private val viewTypeEndOfList: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), PaginationAdapter {

    private val itemSet: LinkedHashSet<T> = LinkedHashSet()

    // Callback para o clique no botão "End of List"
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
        Log.d("BasePaginationAdapter", "Binding item at position $position") // Adicione este log
        if (getItemViewType(position) == viewTypeItem) {
            onBindItemViewHolder(holder as VH, itemSet.elementAt(position))
        }
    }

    override fun getItemCount(): Int = itemSet.size

    override fun getItemViewType(position: Int): Int {
        return when (itemSet.elementAt(position)) {
            is LoadingItem -> viewTypeLoading
            is EndOfListItem -> viewTypeEndOfList
            else -> viewTypeItem
        }
    }

    fun addItems(newItems: List<T>) {
        val startPosition = itemSet.size
        val filteredItems = newItems.filter { it !in itemSet } // Filtra itens já existentes
        itemSet.addAll(filteredItems) // Adiciona apenas itens únicos
        notifyItemRangeInserted(startPosition, filteredItems.size)
    }

    override fun addLoadingView() {
        if (itemSet.isNotEmpty() && itemSet.last() !is LoadingItem) {
            itemSet.add(LoadingItem as T)
            notifyItemInserted(itemSet.size - 1)
        }
    }

    override fun removeLoadingView() {
        if (itemSet.isNotEmpty() && itemSet.last() is LoadingItem) {
            itemSet.remove(itemSet.last())
            notifyItemRemoved(itemSet.size)
        }
    }

    fun addEndOfListView() {
        removeLoadingView()
        if (itemSet.lastOrNull() !is EndOfListItem) {
            itemSet.add(EndOfListItem as T)
            notifyItemInserted(itemSet.size - 1)
            Log.d("BasePaginationAdapter", "End of List item added")
        }
    }

    private fun createLoadingViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return object : RecyclerView.ViewHolder(binding.root) {}
    }
    private fun createEndOfListViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemEndOfListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        binding.endOfListContainer.setOnClickListener {
            Log.d("BasePaginationAdapter", "End of List clicked") // Log para depuração
            onScrollToTopClick?.invoke()
        }
        return object : RecyclerView.ViewHolder(binding.root) {}
    }
}