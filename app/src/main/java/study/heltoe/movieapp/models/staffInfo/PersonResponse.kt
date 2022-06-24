package study.heltoe.movieapp.models.staffInfo

data class PersonResponse(
    val personId: Int,
    val webUrl: String? = null,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val sex: String? = null,
    val posterUrl: String,
    val growth: String? = null,
    val birthday: String? = null,
    val death: String? = null,
    val age: Int? = null,
    val birthplace: String? = null,
    val deathplace: String? = null,
    val hasAwards: Int? = null,
    val profession: String? = null,
    val facts: List<String>,
    val spouses: List<PersonResponseSpouses>,
    val films: List<PersonResponseFilms>
)
