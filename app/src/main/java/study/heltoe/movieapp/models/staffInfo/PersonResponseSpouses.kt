package study.heltoe.movieapp.models.staffInfo

data class PersonResponseSpouses(
    val personId: Int,
    val name: String? = null,
    val divorced: Boolean,
    val divorcedReason: String? = null,
    val sex: String,
    val children: Int,
    val webUrl: String,
    val relation: String
)
