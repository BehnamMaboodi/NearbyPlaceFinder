package me.behna.nearbyplace.viewmodel

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import me.behna.nearbyplace.repository.business.BaseBusinessRepository
import me.behna.nearbyplace.utilities.SystemUtils
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: BaseBusinessRepository) :
    ViewModel() {
    val queryToBeSearched = MutableStateFlow("")
    val searchedQuery = MutableStateFlow("")
    var delayedSearchJob: Job? = null

    fun clearResult() {
        queryToBeSearched.update { "" }
        searchedQuery.update { "" }
    }

    fun onSearchTextChanged() {
        delayedSearchJob?.cancel()
        delayedSearchJob = viewModelScope.launch(Dispatchers.Main) {
            delay(SEARCH_DELAY)
            if (isActive) {
                search()
            }
        }
    }

    private fun search() {

        searchedQuery.update { queryToBeSearched.value }
    }

    fun onImeSearchClick(view: TextView, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            try {
                SystemUtils.hideKeyboard(SystemUtils.getActivity(view.context))
            } catch (_: Exception) {
            }
        }
        return false
    }

    companion object {
        private const val SEARCH_DELAY = 500L
    }
}