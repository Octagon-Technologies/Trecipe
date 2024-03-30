package com.octagon_technologies.trecipe.presentation.ui.search

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.FragmentSearchBinding
import com.octagon_technologies.trecipe.domain.Resource
import com.octagon_technologies.trecipe.presentation.ui.search.autocomplete_group.AutoCompleteGroup
import com.octagon_technologies.trecipe.presentation.ui.search.recent_tab.RecentTabGroup
import com.octagon_technologies.trecipe.utils.KeyboardUtils
import com.octagon_technologies.trecipe.utils.showShortSnackBar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        setUpLoadState()
        setUpRecentAutoComplete()
        setUpAutoCompleteRecyclerView()
        setUpNavigateToResults()
        setUpTextWatcher()
        setUpClickListener()
    }

    private fun setUpNavigateToResults() {
        // Once we've saved the query to the DB, we can navigate to the results page
        viewModel.navigateToSearchResults.observe(viewLifecycleOwner) { searchQuery ->
            if (searchQuery != null) {
                findNavController().navigate(
                    SearchFragmentDirections
                        .actionSearchFragmentToSearchResultsFragment(searchQuery)
                )
                viewModel.resetNavigateToSearchResults()
            }
        }
    }

    private fun setUpLoadState() {
        viewModel.recipeSuggestions.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Error) {
                binding.errorLayout.visibility = View.VISIBLE
                binding.recentLayout.visibility = View.GONE

                showShortSnackBar(result.resMessage)
            } else if (result is Resource.Success) {
                binding.errorLayout.visibility = View.GONE
                binding.searchRecyclerview.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpRecentAutoComplete() {
        val recentSearchGroupAdapter = GroupAdapter<GroupieViewHolder>()
        binding.recentSearchRecyclerview.adapter = recentSearchGroupAdapter

        viewModel.listOfRecentAutoComplete.observe(viewLifecycleOwner) { result ->
            val listOfRecentAutoComplete = result.data

            if (listOfRecentAutoComplete.isNullOrEmpty()) {
                binding.recentLayout.visibility = View.GONE
            } else {
                binding.recentLayout.visibility = View.VISIBLE

                val listOfRecentTabGroup = listOfRecentAutoComplete.map { autoComplete ->
                    RecentTabGroup(
                        recentAutoComplete = autoComplete,
                        onRecentTabSelected = {
                            binding.searchInput.setText(autoComplete.name)
                            viewModel.addQueryToRecent(autoComplete)
                        }
                    )
                }

                recentSearchGroupAdapter.addAll(listOfRecentTabGroup)
            }
        }
    }

    private fun setUpAutoCompleteRecyclerView() {
        val autoCompleteGroupAdapter = GroupAdapter<GroupieViewHolder>()
        binding.searchRecyclerview.adapter = autoCompleteGroupAdapter

        viewModel.recipeSuggestions.observe(viewLifecycleOwner) { result ->
            val recipeSuggestions = result.data

            if (result is Resource.Success && recipeSuggestions != null) {
                autoCompleteGroupAdapter.clear()
                Timber.d("recipeSuggestions.size is ${recipeSuggestions.size}")

                val listOfAutoCompleteGroup = recipeSuggestions.map { autoComplete ->
                    AutoCompleteGroup(autoComplete = autoComplete, onClick = {
                        binding.searchInput.setText(autoComplete.name)
                        viewModel.addQueryToRecent(autoComplete)
                    })
                }

                Timber.d("listOfAutoCompleteGroup.size is ${listOfAutoCompleteGroup.size}")
                autoCompleteGroupAdapter.addAll(listOfAutoCompleteGroup)
            }
        }
    }

    private fun setUpClickListener() {
        binding.searchBackBtn.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setUpTextWatcher() {
        binding.searchInput.setOnEditorActionListener { v, actionId, event ->
            KeyboardUtils.hideKeyboard(binding.searchInput)
            countDownTimer.cancel()

            val query = binding.searchInput.text?.toString()
            if (!query.isNullOrBlank()) {
                viewModel.loadSuggestions(query)
            }
            true
        }

        binding.searchInput.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                menu.clear()
                return true;
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                menu.clear();
                return false;
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return false;
            }

            override fun onDestroyActionMode(mode: ActionMode) {

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.searchInput.customInsertionActionModeCallback = object : ActionMode.Callback {
                override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    menu?.clear()
                    return true
                }

                override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    menu?.clear()
                    return false
                }

                override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                    return false
                }

                override fun onDestroyActionMode(mode: ActionMode?) {

                }
            }
        }

        binding.searchInput.addTextChangedListener {
            countDownTimer.cancel()
            countDownTimer.start()
        }
    }

    private val countDownTimer = object : CountDownTimer(1200, 600) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            val query = binding.searchInput.text?.toString()
            if (!query.isNullOrBlank()) {
                viewModel.loadSuggestions(query)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        KeyboardUtils.showKeyboard(binding.searchInput)
    }

    override fun onStop() {
        super.onStop()
        KeyboardUtils.hideKeyboard(binding.searchInput)
    }

}