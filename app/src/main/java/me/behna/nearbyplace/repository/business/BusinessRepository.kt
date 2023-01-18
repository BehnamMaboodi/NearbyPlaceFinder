package me.behna.nearbyplace.repository.business

import com.haroldadmin.cnradapter.NetworkResponse
import me.behna.nearbyplace.api.RetrofitInstance.Companion.api
import me.behna.nearbyplace.api.YelpApiService
import me.behna.nearbyplace.data.BusinessSearchResultModel
import me.behna.nearbyplace.data.ErrorResultModel

class BusinessRepository(api: YelpApiService) : BaseBusinessRepository(api) {

    override suspend fun searchForBusiness(
        term: String,
        location: String,
        sortBy: String?,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<BusinessSearchResultModel, ErrorResultModel> {
        return api.searchForBusinesses(term, location, sortBy, limit, offset)
    }

}