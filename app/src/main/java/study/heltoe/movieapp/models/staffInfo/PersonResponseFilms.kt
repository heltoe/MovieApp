package study.heltoe.movieapp.models.staffInfo

data class PersonResponseFilms(
    val filmId: Int,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val rating: String? = null,
    val general: Boolean,
    val description: String? = null,
    val professionKey: String
)
