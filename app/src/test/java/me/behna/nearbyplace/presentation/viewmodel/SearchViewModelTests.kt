package me.behna.nearbyplace.presentation.viewmodel

import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.setMain
import me.behna.nearbyplace.data.UiString
import me.behna.nearbyplace.data.model.ui_event.UiEvent
import me.behna.nearbyplace.di.DispatcherProvider
import me.behna.nearbyplace.domain.use_case.SearchForBusinessUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTests {

    val searchUseCase = mockk<SearchForBusinessUseCase>()
    var viewModel: SearchViewModel = SearchViewModel(searchUseCase)

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(DispatcherProvider(true).Main)
    }

    @Test
    fun `clearEverything should clear locationToBeSearched and currentSearchedLocation`() =
        runBlocking {
            viewModel.locationToBeSearched.update { "New York" }
            viewModel.currentSearchedLocation.update { "New York" }
            viewModel.hintMessage.update { UiEvent.ServerError() }

            viewModel.clearEverything()
            delay(5)
            assertEquals("", viewModel.locationToBeSearched.value)
            assertEquals("", viewModel.currentSearchedLocation.value)
            assertEquals(UiString.SEARCH_HINT, viewModel.hintMessage.value.messageStringRes)
        }

    @Test
    fun `onAdapterStateChanged should update hintMessage`() = runBlocking {
        val testEvent = UiEvent.InvalidInput<Any>(UiString.SEARCH_HINT)
        viewModel.onAdapterStateChanged(testEvent)
        delay(5)
        assertEquals(testEvent, viewModel.hintMessage.value)
    }


    @Test
    fun `onSearchTermChange should call searchUseCase validateSearchTerm method`() =
        runBlocking {
            viewModel.locationToBeSearched.update { "New York" }

            viewModel.onSearchTermChange()

            verify(exactly = 1) { searchUseCase.validateSearchTerm("New York") }
        }
}