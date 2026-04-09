package com.example.rickandmorty.commons.pagination

data class PaginatedResult<T>(
    val items: List<T>,
    val hasNextPage: Boolean,
)
