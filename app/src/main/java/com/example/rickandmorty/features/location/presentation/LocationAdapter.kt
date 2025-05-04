package com.example.rickandmorty.features.location.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.commons.utils.pagination.BasePaginationAdapter
import com.example.rickandmorty.databinding.ItemLocationBinding
import com.example.rickandmorty.features.location.data.model.Location

sealed class ListItem {
    data class LocationItem(val location: Location) : ListItem()
}

class LocationAdapter(
    private val context: Context
) : BasePaginationAdapter<ListItem.LocationItem, LocationAdapter.LocationViewHolder>(
    viewTypeItem = 0,
    viewTypeLoading = 1,
    viewTypeEndOfList = 2
) {

    inner class LocationViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(location: Location) {
            binding.locationName.text = location.name
            binding.locationType.text = binding.root.context.getString(R.string.location_type, location.type)
            binding.locationDimension.text = binding.root.context.getString(R.string.location_dimension, location.dimension)
        }
    }

    override fun onCreateItemViewHolder(parent: ViewGroup): LocationViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindItemViewHolder(holder: LocationViewHolder, item: ListItem.LocationItem) {
        holder.bind(item.location)
    }
}