package me.behna.nearbyplace.data.api

import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import me.behna.nearbyplace.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        private const val BASE_URL = "https://api.yelp.com/"

        // We must encrypt this token and store it somewhere safe in a real app
        private const val AUTH_TOKEN =
            "2ROaa2Rh9qu3WVTCms8FoVE4mSfHQHC7QJua95-kKT-PqzIlLSrs4tmHVdtdFw_66-JNfRiJmbCByHTvFNy5dQq-tpfS4FrPpupIzKlgELR3br-r5trpeFhrCRgwWnYx"

        private val retrofit by lazy {
            val authInterceptor = Interceptor {
                val original = it.request()

                val request = original.newBuilder()
                    .header("Authorization", "Bearer $AUTH_TOKEN")
                    .method(original.method, original.body)
                    .build()
                it.proceed(request)
            }
            val logging =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            val client = OkHttpClient.Builder().apply {
                addInterceptor(authInterceptor)
                if (BuildConfig.DEBUG) addInterceptor(logging)
            }.build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(NetworkResponseAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api: YelpApiService by lazy {
            retrofit.create(YelpApiService::class.java)
        }
    }
}