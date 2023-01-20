package me.behna.nearbyplace.presentation.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.behna.nearbyplace.data.model.ErrorResultModel
import me.behna.nearbyplace.data.model.ui_event.UiEvent
import me.behna.nearbyplace.databinding.FragmentSearchBinding
import me.behna.nearbyplace.presentation.adapter.BusinessAdapter
import me.behna.nearbyplace.presentation.viewmodel.SearchViewModel
import me.behna.nearbyplace.utilities.SystemUtils
import java.io.IOException

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {

    lateinit var viewModel: SearchViewModel
    private var _binding: FragmentSearchBinding? = null
    private val adapter = BusinessAdapter()
    private val binding get() = _binding!!
    var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        _binding = FragmentSearchBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.fragment = this
            it.viewModel = viewModel
            it.recyclerView.also { rv ->
                rv.adapter = adapter
                rv.layoutManager = GridLayoutManager(requireContext(), 1)
            }
        }
        // Handling adapter state TODO: create a footer for retry
        adapter.addLoadStateListener { loadState ->
            binding.progressBar.visibility = if (isLoading(
                    loadState.append,
                    loadState.refresh,
                    loadState.prepend
                )
            ) View.VISIBLE else View.GONE

            val error = isError(loadState.append, loadState.prepend, loadState.refresh)
            when (error?.error) {
                is ErrorResultModel -> {
                    lifecycleScope.launch {
                        adapter.submitData(PagingData.empty())
                    }
                    viewModel.onAdapterStateChanged(
                        UiEvent.ServerError(
                            messageStringRes = null,
                            message = (error.error as ErrorResultModel).error.description
                        )
                    )
                }
                is IOException -> {
                    viewModel.onAdapterStateChanged(
                        UiEvent.NetworkError()
                    )
                }
            }
        }
        collectSearchData()
        return binding.root
    }

    // Collecting the search result from view model and submit to adapter
    fun collectSearchData() {
        lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchFlow.collectLatest { paging ->
                    if (paging == null) {
                        adapter.submitData(PagingData.empty())
                    } else {
                        searchJob?.cancel()
                        searchJob = launch {
                            paging.cancellable().collectLatest {
                                adapter.submitData(it)
                            }
                        }
                    }
                }
            }
        }
    }

    fun isLoading(vararg loadState: LoadState): Boolean {
        loadState.forEach {
            if (it is LoadState.Loading) return true
        }
        return false
    }

    fun isError(vararg loadState: LoadState): LoadState.Error? {
        loadState.forEach {
            if (it is LoadState.Error) return it
        }
        return null
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}