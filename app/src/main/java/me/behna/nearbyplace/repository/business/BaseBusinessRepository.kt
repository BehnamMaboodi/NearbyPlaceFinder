package me.behna.nearbyplace.repository.business

import com.haroldadmin.cnradapter.NetworkResponse
import me.behna.nearbyplace.api.YelpApiService
import me.behna.nearbyplace.data.BusinessSearchResultModel
import me.behna.nearbyplace.data.ErrorResultModel
import me.behna.nearbyplace.repository.BaseRepository

abstract class BaseBusinessRepository(api: YelpApiService) : BaseRepository() {
    abstract suspend fun searchForBusiness(
        term: String,
        location: String,
        sortBy: String?,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<BusinessSearchResultModel, ErrorResultModel>
}