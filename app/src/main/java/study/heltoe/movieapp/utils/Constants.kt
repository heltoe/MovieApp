package study.heltoe.movieapp.utils

import study.heltoe.movieapp.client.RetrofitClient
import study.heltoe.movieapp.client.RetrofitServices


object Constants {
    private const val baseUrl = "https://api.kinopoisk.dev/"
    const val token = "ZQQ8GMN-TN54SGK-NB3MKEC-ZKB8V06"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(baseUrl).create(RetrofitServices::class.java)
}