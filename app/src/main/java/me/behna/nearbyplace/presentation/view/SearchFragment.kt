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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import me.behna.nearbyplace.data.model.ErrorResultModel
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

    private val SEARCH_DELAY = 500L
    lateinit var viewModel: SearchViewModel
    private var _binding: FragmentSearchBinding? = null
    private var delayedSearchJob: Job? = null
    private val adapter = BusinessAdapter()

    private val binding get() = _binding!!

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
        adapter.addLoadStateListener { loadState ->

            if (loadState.refresh is LoadState.Loading) {

            } else {
                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                when (error?.error) {
                    is ErrorResultModel -> {
                        lifecycleScope.launch { adapter.submitData(PagingData.empty()) }
                    }
                    is IOException -> {

                    }
                }
            }
        }
        search()
        lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.clearResultEvent.collectLatest {
                    clearResult()
                }
            }
        }
        return binding.root

    }

    suspend fun clearResult() {
        adapter.submitData(PagingData.empty())
    }

    fun search() {
        delayedSearchJob?.cancel()
        delayedSearchJob = lifecycleScope.launch(Dispatchers.Main) {
            delay(SEARCH_DELAY)
            if (isActive) viewModel.search()?.collectLatest {
                adapter.submitData(it)
            }
        }
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