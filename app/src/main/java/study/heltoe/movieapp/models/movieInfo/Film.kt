package study.heltoe.movieapp.models.movieInfo

import study.heltoe.movieapp.models.enums.ProductionStatusEnum
import study.heltoe.movieapp.models.movieList.Country
import study.heltoe.movieapp.models.movieList.Genre
import study.heltoe.movieapp.models.enums.TypeEnum

data class Film(
    val kinopoiskId: Int,
    val imdbId: String? = null,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val nameOriginal: String? = null,
    val posterUrl: String,
    val posterUrlPreview: String,
    val coverUrl: String? = null,
    val logoUrl: String? = null,
    val reviewsCount: Int,
    val ratingGoodReview: Number? = null,
    val ratingGoodReviewVoteCount: Int? = null,
    val ratingKinopoisk: Number? = null,
    val ratingKinopoiskVoteCount: Int? = null,
    val ratingImdb: Number? = null,
    val ratingImdbVoteCount: Int? = null,
    val ratingFilmCritics: Number? = null,
    val ratingFilmCriticsVoteCount: Int? = null,
    val ratingAwait: Number? = null,
    val ratingAwaitCount: Int? = null,
    val ratingRfCritics: Number? = null,
    val ratingRfCriticsVoteCount: Int? = null,
    val webUrl: String,
    val year: Int? = null,
    val filmLength: Int? = null,
    val slogan: String? = null,
    val description: String? = null,
    val shortDescription: String? = null,
    val editorAnnotation: String? = null,
    val isTicketsAvailable: Boolean,
    val productionStatus: ProductionStatusEnum? = null,
    val type: TypeEnum? = null,
    val ratingMpaa: String? = null,
    val ratingAgeLimits: String? = null,
    val hasImax: Boolean? = null,
    val has3D: Boolean? = null,
    val lastSync: String,
    val countries: List<Country>,
    val genres: List<Genre>,
    val startYear: Int? = null,
    val endYear: Int? = null,
    val serial: Boolean? = null,
    val shortFilm: Boolean? = null,
    val completed: Boolean? = null
)