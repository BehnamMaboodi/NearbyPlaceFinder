package me.behna.nearbyplace.data.api

import com.haroldadmin.cnradapter.NetworkResponse
import me.behna.nearbyplace.data.model.BusinessSearchResultModel
import me.behna.nearbyplace.data.model.ErrorResultModel
import retrofit2.http.GET
import retrofit2.http.Query

interface YelpApiService {
    @GET("v3/businesses/search")
    suspend fun searchForBusinesses(
        @Query("term") term: String,
        @Query("location") location: String,
        @Query("sort_by") sortBy: String?,
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?,
    ): NetworkResponse<BusinessSearchResultModel, ErrorResultModel>

}