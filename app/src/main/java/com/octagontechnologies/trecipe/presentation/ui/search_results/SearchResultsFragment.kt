package com.octagontechnologies.trecipe.presentation.ui.search_results

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagontechnologies.trecipe.R
import com.octagontechnologies.trecipe.databinding.FragmentSearchResultsBinding
import com.octagontechnologies.trecipe.domain.Resource
import com.octagontechnologies.trecipe.presentation.ui.search_results.search_result.SearchResultGroup
import com.octagontechnologies.trecipe.utils.BottomNavUtils
import com.octagontechnologies.trecipe.utils.showShortSnackBar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultsFragment : Fragment(R.layout.fragment_search_results) {

    private val viewModel: SearchResultsViewModel by viewModels()

    private lateinit var binding: FragmentSearchResultsBinding

    private val searchResultsGroupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchResultsBinding.bind(view)
        binding.searchResultsInput.text = viewModel.query
        viewModel.loadComplexSearch()

        setUpSearchResults()
        setUpClickListeners()
        setUpSearchRecyclerView()
        setUpLoadState()
    }

    private fun setUpLoadState() {
        viewModel.listOfSearchResult.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success) {
                binding.loadingProgressBar.visibility = View.GONE
                binding.errorLayout.visibility = View.GONE
                binding.searchResultsRecyclerView.visibility = View.VISIBLE
            } else if (result is Resource.Error) {
                binding.loadingProgressBar.visibility = View.GONE
                binding.errorLayout.visibility = View.VISIBLE
                binding.searchResultsRecyclerView.visibility = View.GONE

                showShortSnackBar(result.resMessage)
            }
        }
    }

    private fun setUpSearchResults() {
        viewModel.listOfSearchResult.observe(viewLifecycleOwner) { result ->
            val listOfSearchResult = result.data ?: return@observe
            val listOfSearchGroup = listOfSearchResult.map { searchResult ->
                SearchResultGroup(
                    searchResult = searchResult,
                    openRecipe = {
                        findNavController().navigate(
                            SearchResultsFragmentDirections
                                .actionSearchResultsFragmentToRecipeFragment(searchResult.recipeId)
                        )
                    }
                )
            }

            searchResultsGroupAdapter.addAll(listOfSearchGroup)
        }
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

                    viewModel.checkIfShouldReloadMore(lastPosition)
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