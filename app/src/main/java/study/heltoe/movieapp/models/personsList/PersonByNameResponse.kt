package study.heltoe.movieapp.models.personsList

data class PersonByNameResponse(
    val total: Int,
    val items: MutableList<PersonByNameResponseItems>
)
