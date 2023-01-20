package me.behna.nearbyplace.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import me.behna.nearbyplace.data.UiString
import me.behna.nearbyplace.data.model.BusinessModel
import me.behna.nearbyplace.data.model.ui_event.UiEvent
import me.behna.nearbyplace.domain.use_case.SearchForBusinessUseCase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchUseCase: SearchForBusinessUseCase) :
    ViewModel() {
    val searchFlow: MutableSharedFlow<Flow<PagingData<BusinessModel>>?> =
        MutableStateFlow(null)

    private var delayedSearchJob: Job? = null
    val hintMessage = MutableStateFlow<UiEvent<Any>>(UiEvent.InvalidInput(UiString.SEARCH_HINT))

    val locationToBeSearched = MutableStateFlow("")
    val currentSearchedLocation = MutableStateFlow("")

    fun clearEverything() {
        locationToBeSearched.update { "" }
        currentSearchedLocation.update { "" }
        hintMessage.update { UiEvent.InvalidInput(UiString.SEARCH_HINT) }
        clearItems()
    }

    fun onAdapterStateChanged(event: UiEvent<Any>) {
        viewModelScope.launch {
            hintMessage.emit(event)
        }
    }

    fun clearItems() {
        viewModelScope.launch { searchFlow.emit(null) }
    }


    // will be called on input text changes
    fun onSearchTermChange() {
        delayedSearchJob?.cancel()
        delayedSearchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            if (isActive) search()
        }
    }

    suspend fun search() {
        when (searchUseCase.validateSearchTerm(locationToBeSearched.value)) {
            is UiEvent.Error<*> -> {
                hintMessage.update { it }
                clearEverything()
                return
            }
            is UiEvent.Success<*> -> {
                clearItems()
                currentSearchedLocation.update { locationToBeSearched.value }
                searchFlow.emit(
                    searchUseCase(
                        currentSearchedLocation.value,
                        coroutineScope = viewModelScope
                    ).cachedIn(viewModelScope)
                )
                hintMessage.update { UiEvent.Success() }
            }
        }
    }
}

