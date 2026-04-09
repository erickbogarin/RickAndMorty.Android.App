package com.example.rickandmorty.features.location.domain

import com.example.rickandmorty.commons.pagination.PaginatedResult
import com.example.rickandmorty.features.location.data.LocationRepository
import com.example.rickandmorty.features.location.data.model.Location
import io.reactivex.Single
import javax.inject.Inject

interface GetLocationsUseCase {
    fun execute(page: Int): Single<PaginatedResult<Location>>
}

class GetLocationsUseCaseImpl @Inject constructor(
    private val repository: LocationRepository,
) : GetLocationsUseCase {
    override fun execute(page: Int): Single<PaginatedResult<Location>> = repository.getAllLocations(page)
}
