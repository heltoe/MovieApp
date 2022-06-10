package study.heltoe.movieapp.models

data class Movie(
    val alternativeName: String? = null,
    val description: String? = null,
    val enName: String? = null,
    val externalId: ExternalId,
    val id: Int,
    val movieLength: Int? = null,
    val name: String? = null,
    val names: List<Name>,
    val poster: Poster,
    val rating: Rating,
    val shortDescription: String? = null,
    val type: String? = null,
    val votes: Votes,
    val year: Int? = null,
)