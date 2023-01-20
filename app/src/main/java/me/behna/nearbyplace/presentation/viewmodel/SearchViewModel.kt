package me.behna.nearbyplace.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.behna.nearbyplace.R
import me.behna.nearbyplace.data.model.BusinessModel
import me.behna.nearbyplace.data.model.ui_event.UiEvent
import me.behna.nearbyplace.domain.use_case.SearchForBusinessUseCase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchUseCase: SearchForBusinessUseCase) :
    ViewModel() {
    private var currentSearchResult: Flow<PagingData<BusinessModel>>? = null
    val hintMessage = MutableStateFlow<UiEvent<Any>>(UiEvent.InvalidInput(R.string.search_hint))
    private val _clearResultEvent = MutableSharedFlow<UiEvent.Refresh<Any>>()
    val clearResultEvent: SharedFlow<UiEvent.Refresh<Any>> = _clearResultEvent

    val locationToBeSearched = MutableStateFlow("")
    val currentSearchedLocation = MutableStateFlow("")

    fun clearEverything() {
        locationToBeSearched.update { "" }
        currentSearchedLocation.update { "" }
        clearItems()
    }

    fun clearItems() {
        viewModelScope.launch {
            _clearResultEvent.emit(UiEvent.Refresh())
        }
    }


    fun search(): Flow<PagingData<BusinessModel>>? {
        if (currentSearchedLocation.value == locationToBeSearched.value && currentSearchResult != null) {
            return currentSearchResult as Flow<PagingData<BusinessModel>>
        }
        return when (searchUseCase.validateSearchTerm(locationToBeSearched.value)) {
            is UiEvent.Error<*> -> {
                hintMessage.update { it }
                clearEverything()
                null
            }
            is UiEvent.Success<*> -> {
                clearItems()
                currentSearchedLocation.update { locationToBeSearched.value }
                currentSearchResult =
                    searchUseCase(currentSearchedLocation.value).cachedIn(viewModelScope)
                hintMessage.update { UiEvent.Success() }
                currentSearchResult as Flow<PagingData<BusinessModel>>
            }
        }
    }

}