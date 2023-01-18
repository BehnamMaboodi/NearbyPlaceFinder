package me.behna.nearbyplace.data.repository.business

import com.haroldadmin.cnradapter.NetworkResponse
import me.behna.nearbyplace.data.api.RetrofitInstance.Companion.api
import me.behna.nearbyplace.data.api.YelpApiService
import me.behna.nearbyplace.data.model.BusinessSearchResultModel
import me.behna.nearbyplace.data.model.ErrorResultModel

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