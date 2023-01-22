package me.behna.nearbyplace.domain.use_case

import com.haroldadmin.cnradapter.NetworkResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import me.behna.nearbyplace.data.model.BusinessModel
import me.behna.nearbyplace.data.model.BusinessSearchResultModel
import me.behna.nearbyplace.data.model.ErrorResultModel
import me.behna.nearbyplace.data.paging.PagingKey
import me.behna.nearbyplace.domain.use_case.BusinessPagingUseCase.getError
import me.behna.nearbyplace.domain.use_case.BusinessPagingUseCase.getNextKey
import me.behna.nearbyplace.domain.use_case.BusinessPagingUseCase.getPrevKey
import me.behna.nearbyplace.domain.use_case.BusinessPagingUseCase.mergeLists
import java.io.IOException

class BusinessPagingUseCaseTest : BehaviorSpec({
    given("a list of businesses, total and current key") {
        val businesses = listOf(BusinessModel(), BusinessModel(), BusinessModel())
        val total = 100
        val currentKey = PagingKey(total, 0)
        val body = BusinessSearchResultModel(businesses, total = total)
        `when`("getting next key for success response") {
            val response =
                NetworkResponse.Success<BusinessSearchResultModel, ErrorResultModel>(body)
            val newKey = getNextKey(response, currentKey)
            then("new key should be returned") {
                newKey shouldBe PagingKey(total, 3)
            }
        }
        `when`("getting next key for success response with no more pages") {
            val response =
                NetworkResponse.Success<BusinessSearchResultModel, ErrorResultModel>(body.copy(total = 3))
            val newKey = getNextKey(response, currentKey)
            then("new key should be null") {
                newKey shouldBe null
            }
        }
        `when`("getting next key for error response") {
            val response = NetworkResponse.ServerError<BusinessSearchResultModel, ErrorResultModel>(
                ErrorResultModel()
            )
            val newKey = getNextKey(response, currentKey)
            then("current key should be returned") {
                newKey shouldBe currentKey
            }
        }

    }

    given("a list of businesses, total and current key") {
        val businesses = listOf(BusinessModel(), BusinessModel(), BusinessModel())
        val total = 100
        val currentKey = PagingKey(total, 10)
        val body = BusinessSearchResultModel(businesses, total = total)
        `when`("getting previous key for success response") {
            val response =
                NetworkResponse.Success<BusinessSearchResultModel, ErrorResultModel>(body)
            val newKey = getPrevKey(response, currentKey)
            then("new key should be returned") {
                newKey shouldBe PagingKey(total, 7)
            }
        }
        `when`("getting previous key for success response with no more pages") {
            val response =
                NetworkResponse.Success<BusinessSearchResultModel, ErrorResultModel>(body)
            val newKey = getPrevKey(response, PagingKey(total, 0))
            then("new key should be null") {
                newKey shouldBe null
            }
        }
        `when`("getting previous key for error response") {
            val response = NetworkResponse.ServerError<BusinessSearchResultModel, ErrorResultModel>(
                ErrorResultModel()
            )
            val newKey = getPrevKey(response, currentKey)
            then("current key should be returned") {
                newKey shouldBe currentKey
            }
        }
        `when`("getting previous key for success response with current key offset of 0") {
            val response =
                NetworkResponse.Success<BusinessSearchResultModel, ErrorResultModel>(body)
            val newKey = getPrevKey(response, PagingKey(total, 0))
            then("new key should be null") {
                newKey shouldBe null
            }
        }
        `when`("getting previous key with null response") {
            val newKey = getPrevKey(null, currentKey)
            then("current key should be returned") {
                newKey shouldBe currentKey
            }
        }
    }

    given("various network responses") {
        val networkError =
            NetworkResponse.NetworkError<BusinessSearchResultModel, ErrorResultModel>(IOException())
        val serverError = NetworkResponse.ServerError<BusinessSearchResultModel, ErrorResultModel>(
            ErrorResultModel()
        )
        val successResponse = NetworkResponse.Success<BusinessSearchResultModel, ErrorResultModel>(
            BusinessSearchResultModel(listOf())
        )
        `when`("getting error for network error response") {
            val error = getError(networkError)
            then("IOException should be returned") {
                error shouldBe IOException()
            }
        }
        `when`("getting error for server error response") {
            val error = getError(serverError)
            then("ErrorResultModel should be returned") {
                error shouldBe serverError.body
            }
        }
        `when`("getting error for multiple responses with network error") {
            val error = getError(successResponse, successResponse, networkError)
            then("IOException should be returned") {
                error shouldBe IOException()
            }
        }
        `when`("getting error for multiple responses with server error") {
            val error = getError(successResponse, serverError, successResponse)
            then("ErrorResultModel should be returned") {
                error shouldBe ErrorResultModel()
            }
        }
        `when`("getting error for success responses") {
            val error = getError(successResponse, successResponse)
            then("Null should be returned") {
                error shouldBe null
            }
        }
    }

    given("various network responses") {
        val successResponse1 = NetworkResponse.Success<BusinessSearchResultModel, ErrorResultModel>(
            BusinessSearchResultModel(
                listOf(
                    BusinessModel("Business 1"),
                    BusinessModel("Business 2")
                )
            )
        )
        val successResponse2 = NetworkResponse.Success<BusinessSearchResultModel, ErrorResultModel>(
            BusinessSearchResultModel(
                listOf(
                    BusinessModel("Business 3"),
                    BusinessModel("Business 4"),
                    BusinessModel("Business 5")
                )
            )
        )
        val errorResponse1 =
            NetworkResponse.ServerError<BusinessSearchResultModel, ErrorResultModel>(
                ErrorResultModel()
            )
        val errorResponse2 =
            NetworkResponse.NetworkError<BusinessSearchResultModel, ErrorResultModel>(IOException())
        `when`("merging lists for success responses") {
            val mergedList = mergeLists(successResponse1, successResponse2)
            then("a list containing all businesses from both responses should be returned") {
                mergedList shouldBe listOf(
                    BusinessModel("Business 1"),
                    BusinessModel("Business 2"),
                    BusinessModel("Business 3"),
                    BusinessModel("Business 4"),
                    BusinessModel("Business 5")
                )
            }
        }
        `when`("merging lists for mixed success and error responses") {
            val mergedList =
                mergeLists(successResponse1, errorResponse1, successResponse2, errorResponse2)
            then("a list containing all businesses from the success responses should be returned") {
                mergedList shouldBe listOf(
                    BusinessModel("Business 1"),
                    BusinessModel("Business 2"),
                    BusinessModel("Business 3"),
                    BusinessModel("Business 4"),
                    BusinessModel("Business 5")
                )
            }
        }
        `when`("merging lists for all error responses") {
            val mergedList = mergeLists(errorResponse1, errorResponse2)
            then("an empty list should be returned") {
                mergedList shouldBe emptyList()
            }
        }
        `when`("merging lists with null responses") {
            val mergedList =
                mergeLists(*arrayOfNulls<NetworkResponse<BusinessSearchResultModel, *>>(3))
            then("an empty list should be returned") {
                mergedList shouldBe emptyList()
            }
        }
    }
})