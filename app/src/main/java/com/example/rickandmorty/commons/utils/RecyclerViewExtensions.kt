package com.example.rickandmorty.commons.utils

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.enableScrollToTop(onScrollToTopClick: (() -> Unit)?) {
    onScrollToTopClick?.let { callback ->
        this.setOnClickListener {
            this.smoothScrollToPosition(0)
            callback()
        }
    }
}
