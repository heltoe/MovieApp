package study.heltoe.movieapp.models.movieInfo

data class MovieInfo(
    val ageRating: Any? = null,
    val alternativeName: String? = null,
    val backdrop: Backdrop,
    val budget: Budget,
    val collections: List<Any>,
    val countries: List<Country>,
    val createDate: String? = null,
    val createdAt: String? = null,
    val description: String? = null,
    val enName: String? = null,
    val externalId: ExternalId,
    val facts: List<Any>,
    val fees: Fees,
    val genres: List<Any>,
    val id: Int,
    val imagesInfo: ImagesInfo,
    val lists: List<Any>,
    val logo: Logo,
    val movieLength: Int? = null,
    val name: String? = null,
    val names: List<Name>,
    val persons: List<Person>,
    val poster: Poster,
    val premiere: Premiere,
    val productionCompanies: List<Any>,
    val rating: Rating,
    val ratingMpaa: Any,
    val seasonsInfo: List<SeasonsInfo>,
    val sequelsAndPrequels: List<Any>,
    val shortDescription: String? = null,
    val similarMovies: List<Any>,
    val slogan: String? = null,
    val spokenLanguages: List<Any>,
    val technology: Technology,
    val ticketsOnSale: Boolean,
    val type: String? = null,
    val typeNumber: Int? = null,
    val updateDates: List<Any>,
    val updatedAt: String,
    val videos: Videos,
    val votes: Votes,
    val year: Int? = null
)