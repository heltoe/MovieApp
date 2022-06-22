package study.heltoe.movieapp.models.personsList

data class PersonByNameResponseItems(
    val kinopoiskId: Int,
    val webUrl: String,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val sex: String? = null,
    val posterUrl: String
)
