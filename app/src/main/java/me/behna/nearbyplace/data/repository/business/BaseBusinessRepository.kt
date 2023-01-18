package me.behna.nearbyplace.data.repository.business

import com.haroldadmin.cnradapter.NetworkResponse
import me.behna.nearbyplace.data.api.YelpApiService
import me.behna.nearbyplace.data.model.BusinessSearchResultModel
import me.behna.nearbyplace.data.model.ErrorResultModel
import me.behna.nearbyplace.data.repository.BaseRepository

abstract class BaseBusinessRepository(api: YelpApiService) : BaseRepository() {
    abstract suspend fun searchForBusiness(
        term: String,
        location: String,
        sortBy: String?,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<BusinessSearchResultModel, ErrorResultModel>
}