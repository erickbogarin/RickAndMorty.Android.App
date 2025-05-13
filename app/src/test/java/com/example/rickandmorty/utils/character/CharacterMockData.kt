package com.example.rickandmorty.utils.character

import com.example.rickandmorty.features.character.data.model.Character
import com.example.rickandmorty.features.character.data.model.Info
import com.example.rickandmorty.features.character.data.model.Location
import com.example.rickandmorty.features.character.data.model.Origin

fun createMockInfo(
    count: Int = 2,
    pages: Int = 1,
    next: String? = null,
    prev: String? = null,
): Info {
    return Info(count, pages, next, prev)
}

fun createMockCharacter(
    id: Int,
    name: String,
    status: String = "Alive",
    species: String = "Human",
    type: String = "",
    gender: String = "Male",
    origin: Origin = createMockOrigin(),
    location: Location = createMockLocation(),
    image: String = "https://example.com/$name.png",
    episode: List<String> = emptyList(),
    url: String = "https://example.com/$name",
    created: String = "2023-01-01T00:00:00Z",
): Character {
    return Character(id, name, status, species, type, gender, origin, location, image, episode, url, created)
}

fun createMockOrigin(
    name: String = "Earth",
    url: String = "https://example.com/earth",
): Origin {
    return Origin(name, url)
}

fun createMockLocation(
    name: String = "Earth",
    url: String = "https://example.com/earth",
): Location {
    return Location(name, url)
}
