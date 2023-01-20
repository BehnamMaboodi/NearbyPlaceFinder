package me.behna.nearbyplace.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.behna.nearbyplace.data.api.YelpApiService
import me.behna.nearbyplace.data.model.BusinessModel
import me.behna.nearbyplace.data.model.BusinessSearchResultModel
import me.behna.nearbyplace.data.model.ErrorResultModel
import me.behna.nearbyplace.domain.use_case.BusinessPagingUseCase


private const val STARTING_PAGE_OFFSET = 0
private const val PIZZA = "Pizza"
private const val BEER = "Beer"

class BusinessPagingSource(
    private val api: YelpApiService,
    private val location: String,
    private val sortBy: String?,
    private val coroutineScope: CoroutineScope
) : PagingSource<HashMap<String, PagingKey>, BusinessModel>() {

    override suspend fun load(params: LoadParams<HashMap<String, PagingKey>>): LoadResult<HashMap<String, PagingKey>, BusinessModel> {
        // get previous key
        val pizzaKey = params.key?.get(PIZZA) ?: PagingKey(null, STARTING_PAGE_OFFSET)
        val beerKey = params.key?.get(BEER) ?: PagingKey(null, STARTING_PAGE_OFFSET)
        var pizzaResponse: NetworkResponse<BusinessSearchResultModel, ErrorResultModel>? = null
        var beerResponse: NetworkResponse<BusinessSearchResultModel, ErrorResultModel>? = null
        // Load the next list if there are any items left (offset < total) and load a new list
        val pizzaJob = coroutineScope.launch(Dispatchers.IO) {
            if (pizzaKey.total == null || pizzaKey.offset < pizzaKey.total!!)
                pizzaResponse =
                    api.searchForBusinesses(
                        PIZZA,
                        location,
                        sortBy,
                        params.loadSize,
                        pizzaKey.offset
                    )
        }
        val beerJob = coroutineScope.launch(Dispatchers.IO) {
            if (beerKey.total == null || beerKey.offset < beerKey.total!!)
                beerResponse =
                    api.searchForBusinesses(BEER, location, sortBy, params.loadSize, beerKey.offset)
        }
        pizzaJob.join()
        beerJob.join()
        val nextKey = HashMap<String, PagingKey>()
        val prevKey = HashMap<String, PagingKey>()
        val mergedBusinessList = BusinessPagingUseCase.mergeLists(pizzaResponse, beerResponse)

        BusinessPagingUseCase.getNextKey(beerResponse, beerKey)
            ?.let { nextKey.put(BEER, it) }
        BusinessPagingUseCase.getNextKey(pizzaResponse, pizzaKey)
            ?.let { nextKey.put(PIZZA, it) }
        BusinessPagingUseCase.getPrevKey(beerResponse, beerKey)
            ?.let { prevKey.put(BEER, it) }
        BusinessPagingUseCase.getPrevKey(pizzaResponse, pizzaKey)
            ?.let { prevKey.put(PIZZA, it) }

        if (mergedBusinessList.isNotEmpty()) {
            return LoadResult.Page(
                data = mergedBusinessList,
                prevKey = prevKey.ifEmpty { null },
                nextKey = nextKey.ifEmpty { null }
            )
        }
        return LoadResult.Error(BusinessPagingUseCase.getError(beerResponse, pizzaResponse))

    }

    override fun getRefreshKey(state: PagingState<HashMap<String, PagingKey>, BusinessModel>): HashMap<String, PagingKey>? {
        return null
    }


}
