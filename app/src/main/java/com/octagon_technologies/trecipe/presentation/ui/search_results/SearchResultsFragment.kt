package com.octagon_technologies.trecipe.presentation.ui.search_results

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.FragmentSearchResultsBinding
import com.octagon_technologies.trecipe.domain.State
import com.octagon_technologies.trecipe.presentation.ui.search_results.search_result.SearchResultGroup
import com.octagon_technologies.trecipe.utils.BottomNavUtils
import com.octagon_technologies.trecipe.utils.ResUtils
import com.octagon_technologies.trecipe.utils.setUpSnackBars
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultsFragment : Fragment(R.layout.fragment_search_results) {

    private val viewModel: SearchResultsViewModel by viewModels()
    private val query by lazy { navArgs<SearchResultsFragmentArgs>().value.query }

    private lateinit var binding: FragmentSearchResultsBinding

    private val searchResultsGroupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchResultsBinding.bind(view)
        binding.searchResultsInput.text = query
        viewModel.loadComplexSearch(query)

        setUpSearchResults()
        setUpClickListeners()
        setUpSearchRecyclerView()
        handleStateChanges()
    }

    private fun setUpSearchResults() {
        viewModel.listOfSearchResult.observe(viewLifecycleOwner) { listOfSearchResult ->
            val listOfSearchGroup = listOfSearchResult?.map { searchResult ->
                SearchResultGroup(
                    searchResult = searchResult,
                    openRecipe = {
                        findNavController().navigate(
                            SearchResultsFragmentDirections
                                .actionSearchResultsFragmentToRecipeFragment(searchResult.recipeId)
                        )
                    }
                )
            } ?: return@observe

            searchResultsGroupAdapter.addAll(listOfSearchGroup)
        }
    }

    private fun handleStateChanges() {
        viewModel.state.setUpSnackBars(this)

        viewModel.state.observe(viewLifecycleOwner) {
            when (it ?: return@observe) {
                State.Empty -> handleEmptyResults()
                State.Loading -> {}

                // Show the recyclerview since we have the search results
                State.Done -> {
                    binding.searchResultsRecyclerView.visibility = View.VISIBLE
                    binding.loadingProgressBar.visibility = View.GONE
                }

                // In case of any errors, show the user
                State.ApiError -> hideLoadingBarAndShowError()
                State.NoNetworkError -> hideLoadingBarAndShowError()
            }
        }
    }

    private fun hideLoadingBarAndShowError() {
        binding.errorOccurredImage.visibility = View.VISIBLE
        binding.errorOccurredDescription.visibility = View.VISIBLE
        binding.loadingProgressBar.visibility = View.GONE
    }

    private fun handleEmptyResults() {
        hideLoadingBarAndShowError()

        binding.errorOccurredImage.setImageResource(R.drawable.inbox_24)
        binding.errorOccurredDescription.text =
            ResUtils.getResString(context, R.string.no_recipes_found_for_search_query)
    }

    private fun setUpClickListeners() {
        binding.searchResultsBackBtn.setOnClickListener {
            findNavController().popBackStack(R.id.discoverFragment, false)
        }
        binding.searchResultsInput.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setUpSearchRecyclerView() {
        binding.searchResultsRecyclerView.adapter = searchResultsGroupAdapter
        setUpSearchRecyclerViewScrollListener()
    }

    private fun setUpSearchRecyclerViewScrollListener() {
        binding.searchResultsRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
                    val lastPosition = gridLayoutManager.findLastVisibleItemPosition()

                    viewModel.checkIfShouldReloadMore(query, lastPosition)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // if dy > 0, the user is scrolling down
                if (dy > 0)
                    BottomNavUtils.hideBottomNavView(activity)
                else if (dy < 0)
                    BottomNavUtils.showBottomNavView(activity)
            }
        })
    }
}