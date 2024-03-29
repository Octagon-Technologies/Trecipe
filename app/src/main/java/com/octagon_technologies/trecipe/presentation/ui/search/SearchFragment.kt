package com.octagon_technologies.trecipe.presentation.ui.search

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.FragmentSearchBinding
import com.octagon_technologies.trecipe.domain.State
import com.octagon_technologies.trecipe.presentation.ui.search.autocomplete_group.AutoCompleteGroup
import com.octagon_technologies.trecipe.presentation.ui.search.recent_tab.RecentTabGroup
import com.octagon_technologies.trecipe.utils.KeyboardUtils
import com.octagon_technologies.trecipe.utils.ResUtils
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

        setUpRecyclerView()
        setUpNavigateToResults()
        setUpTextWatcher()
        setUpClickListener()
        handleStateChanges()
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

    private fun setUpRecyclerView() {
        setUpRecentAutoComplete()
        setUpAutoCompleteRecyclerView()
    }

    private fun setUpRecentAutoComplete() {
        val recentSearchGroupAdapter = GroupAdapter<GroupieViewHolder>()
        binding.recentSearchRecyclerview.adapter = recentSearchGroupAdapter

        viewModel.listOfRecentAutoComplete.observe(viewLifecycleOwner) { listOfRecentAutoComplete ->
            if (listOfRecentAutoComplete.isNullOrEmpty()) {
                binding.recentLayout.visibility = View.GONE
            } else {
                binding.recentLayout.visibility = View.VISIBLE

                val listOfRecentTabGroup = listOfRecentAutoComplete.map { autoComplete ->
                    RecentTabGroup(
                        recentAutoComplete = autoComplete,
                        onRecentTabSelected = { viewModel.addQueryToRecent(autoComplete) }
                    )
                }

                recentSearchGroupAdapter.addAll(listOfRecentTabGroup)
            }
        }
    }

    private fun setUpAutoCompleteRecyclerView() {
        val autoCompleteGroupAdapter = GroupAdapter<GroupieViewHolder>()
        binding.searchRecyclerview.adapter = autoCompleteGroupAdapter

        viewModel.recipeSuggestions.observe(viewLifecycleOwner) { recipeSuggestions ->
            autoCompleteGroupAdapter.clear()
            Timber.d("recipeSuggestions.size is ${recipeSuggestions.size}")

            val listOfAutoCompleteGroup = recipeSuggestions?.map { autoComplete ->
                AutoCompleteGroup(autoComplete = autoComplete, onClick = {

                    binding.searchInput.setText(autoComplete.name)
                    viewModel.addQueryToRecent(autoComplete)

                })
            } ?: return@observe

            Timber.d("listOfAutoCompleteGroup.size is ${listOfAutoCompleteGroup.size}")
            autoCompleteGroupAdapter.addAll(listOfAutoCompleteGroup)
        }
    }

    private fun handleStateChanges() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it ?: return@observe) {
                State.Loading -> {}
                State.Done -> {
                    hideErrorImage()
                    binding.searchRecyclerview.visibility = View.VISIBLE
                }

                State.Empty -> {
                    showErrorImage()
                    binding.errorOccurredImage.setImageResource(R.drawable.inbox_24)
                    binding.errorOccurredDescription.text =
                        ResUtils.getResString(context, R.string.no_recipes_found_for_search_query)
                }

                else -> showErrorImage()
            }
        }
    }

    private fun showErrorImage() {
        binding.errorOccurredImage.visibility = View.VISIBLE
        binding.errorOccurredDescription.visibility = View.VISIBLE
    }

    private fun hideErrorImage() {
        binding.errorOccurredImage.visibility = View.GONE
        binding.errorOccurredDescription.visibility = View.GONE
    }

    private fun setUpClickListener() {
        binding.searchBackBtn.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setUpTextWatcher() {
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