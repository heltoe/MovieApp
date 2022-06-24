package study.heltoe.movieapp.client

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import study.heltoe.movieapp.utils.Constants.BASE_URL_UNOFICIAL
import study.heltoe.movieapp.utils.Constants.TOKEN_UNOFICIAL

class RetrofitClient {
    companion object {
        private val retrofit by lazy {
            // add log to logcat
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor { chain ->
                    val headers = Headers.Builder()
                    headers.add("X-API-KEY: $TOKEN_UNOFICIAL")
                    headers.add("Content-Type", "application/json")

                    val request = chain
                        .request()
                        .newBuilder()
                        .headers(headers.build())
                        .build()

                    return@addInterceptor chain.proceed(request)
                }
                .build()

            // retrofit instance
            Retrofit.Builder()
                .baseUrl(BASE_URL_UNOFICIAL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            retrofit.create(MovieApi::class.java)
        }
    }
}