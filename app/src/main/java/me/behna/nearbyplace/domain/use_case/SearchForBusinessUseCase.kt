package me.behna.nearbyplace.domain.use_case

import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import me.behna.nearbyplace.data.UiString
import me.behna.nearbyplace.data.model.BusinessModel
import me.behna.nearbyplace.data.model.ui_event.UiEvent
import me.behna.nearbyplace.data.repository.business.BusinessRepository

class SearchForBusinessUseCase(private val repository: BusinessRepository) : UseCase {

    operator fun invoke(
        location: String,
        sortBy: String? = null,
        coroutineScope: CoroutineScope
    ): Flow<PagingData<BusinessModel>> {
        return repository.getResultStream(location, sortBy, coroutineScope)
    }

    fun validateSearchTerm(term: String): UiEvent<Any> {
        if (term.isEmpty()) return UiEvent.InvalidInput(UiString.INVALID_INPUT)
        return UiEvent.Success()
    }

}