package me.behna.nearbyplace.domain.use_case

import com.haroldadmin.cnradapter.NetworkResponse
import me.behna.nearbyplace.data.model.BusinessModel
import me.behna.nearbyplace.data.model.BusinessSearchResultModel
import me.behna.nearbyplace.data.model.ErrorResultModel
import me.behna.nearbyplace.data.paging.PagingKey
import java.io.IOException

object BusinessPagingUseCase {

    fun getNextKey(
        response: NetworkResponse<BusinessSearchResultModel, ErrorResultModel>?,
        currentKey: PagingKey
    ): PagingKey? {
        return when (response) {
            is NetworkResponse.Success -> {
                val newOffset = currentKey.offset + response.body.businesses.size
                if (newOffset >= response.body.total) null
                else PagingKey(response.body.total, newOffset)
            }
            else -> currentKey
        }
    }

    fun getPrevKey(
        response: NetworkResponse<BusinessSearchResultModel, ErrorResultModel>?,
        currentKey: PagingKey
    ): PagingKey? {
        return when (response) {
            is NetworkResponse.Success -> {
                val newOffset = currentKey.offset - response.body.businesses.size
                if (newOffset <= 0) null
                else PagingKey(response.body.total, newOffset)
            }
            else -> currentKey
        }
    }


    fun getError(vararg response: NetworkResponse<BusinessSearchResultModel, ErrorResultModel>?): Throwable {
        response.forEach {
            if (it is NetworkResponse.NetworkError)
                return IOException()
        }
        response.forEach {
            if (it is NetworkResponse.ServerError)
                return it.body ?: ErrorResultModel()
        }
        return ErrorResultModel()


    }

    fun mergeLists(vararg response: NetworkResponse<BusinessSearchResultModel, *>?): List<BusinessModel> {
        return response.filterIsInstance<NetworkResponse.Success<BusinessSearchResultModel, *>>()
            .flatMap { it.body.businesses }
    }
}