package me.behna.nearbyplace.presentation.viewmodel

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import me.behna.nearbyplace.data.repository.business.BaseBusinessRepository
import me.behna.nearbyplace.utilities.SystemUtils
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: BaseBusinessRepository) :
    ViewModel() {
    val addressToBeSearched = MutableStateFlow("")
    val searchedAddress = MutableStateFlow("")
    var delayedSearchJob: Job? = null

    fun clearResult() {
        addressToBeSearched.update { "" }
        searchedAddress.update { "" }
    }

    fun onSearchAddressChanged() {
        delayedSearchJob?.cancel()
        delayedSearchJob = viewModelScope.launch(Dispatchers.Main) {
            delay(SEARCH_DELAY)
            if (isActive) {
                search()
            }
        }
    }

    private fun search() {
        viewModelScope.launch(Dispatchers.IO) {

            val pizza =
                repository.searchForBusiness("pizza", addressToBeSearched.value, null, 20, 0)
            if (pizza is NetworkResponse.Success) {
                if (pizza.body.businesses.isNotEmpty())
                    Timber.w("pizza : " + pizza.body.businesses[0].name)
            }
        }
        searchedAddress.update { addressToBeSearched.value }
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