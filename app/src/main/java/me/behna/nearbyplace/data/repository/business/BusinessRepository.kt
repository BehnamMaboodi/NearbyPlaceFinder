package me.behna.nearbyplace.data.repository.business

import android.annotation.SuppressLint
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import me.behna.nearbyplace.data.api.YelpApiService
import me.behna.nearbyplace.data.model.BusinessModel
import me.behna.nearbyplace.data.paging.BusinessPagingSource
import me.behna.nearbyplace.utilities.Constants.SEARCH_BUSINESS_PAGE_SIZE

class BusinessRepository(private val api: YelpApiService) {

    @SuppressLint("CheckResult")
    fun getResultStream(
        location: String,
        sortBy: String?,
        scope: CoroutineScope
    ): Flow<PagingData<BusinessModel>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = SEARCH_BUSINESS_PAGE_SIZE),
            pagingSourceFactory = { BusinessPagingSource(api, location, sortBy, scope) }
        ).flow
    }

}