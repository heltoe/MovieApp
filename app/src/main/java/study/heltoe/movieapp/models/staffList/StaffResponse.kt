package study.heltoe.movieapp.models.staffList

data class StaffResponse(
    val staffId: Int,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val description: String? = null,
    val posterUrl: String,
    val professionText: String,
    val professionKey: String,
)
