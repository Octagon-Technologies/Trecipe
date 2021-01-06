package com.octagon_technologies.trecipe.presentation.ui.search_results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.FragmentSearchResultsBinding
import com.octagon_technologies.trecipe.models.State
import com.octagon_technologies.trecipe.presentation.ui.search_results.search_result_item.SearchResultItem
import com.octagon_technologies.trecipe.repo.network.models.complex_search.SearchResult
import com.octagon_technologies.trecipe.repo.network.models.suggestions.RecipeSuggestion
import com.octagon_technologies.trecipe.utils.BottomNavUtils
import com.octagon_technologies.trecipe.utils.ResUtils
import com.octagon_technologies.trecipe.utils.SelectedRecipeUtils
import com.octagon_technologies.trecipe.utils.ViewUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultsFragment : Fragment() {

    @Inject
    lateinit var selectedRecipeUtils: SelectedRecipeUtils

    private val viewModel: SearchResultsViewModel by viewModels()
    private val args by navArgs<SearchResultsFragmentArgs>()
    private lateinit var binding: FragmentSearchResultsBinding
    private lateinit var recipeSuggestion: RecipeSuggestion

    private val filterGroupAdapter = GroupAdapter<GroupieViewHolder>()
    private val searchResultsGroupAdapter = GroupAdapter<GroupieViewHolder>()

    private val addedSearchResults = ArrayList<SearchResult>()
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchResultsBinding.inflate(inflater)
        recipeSuggestion = args.recipeSuggestion
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated called")
        binding.searchResultsInput.setText(args.query)

        setUpClickListeners()
        setUpRecyclerView()
        setUpSearchRecyclerViewScrollListener()
        handleStateChanges()
        selectedRecipeUtils.handleSelectedStateChanges(this)

        recipeSuggestion.title?.let { viewModel.loadComplexSearch(it) }

        // Get search results and reset loading
        viewModel.complexSearch.observe(viewLifecycleOwner) { complexSearch ->
            complexSearch?.searchResults
                ?.also {
                    isLoading = false
                    if (it.isEmpty()) {
                        handleEmptyResults()
                    }
                }
                ?.filter { it !in addedSearchResults }
                ?.forEach {
                    val searchResultItem = SearchResultItem(it) { onClickSearchResult ->
                        selectedRecipeUtils.getSelectedRecipeFromRecipe(onClickSearchResult.id)
                    }

                    addedSearchResults.add(it)
                    searchResultsGroupAdapter.add(searchResultItem)
                }
        }

        selectedRecipeUtils.selectedRecipe.observe(viewLifecycleOwner) {
            findNavController().navigate(
                SearchResultsFragmentDirections
                    .actionSearchResultsFragmentToNavigationRecipeFragment(it ?: return@observe)
            )
            selectedRecipeUtils.resetSelectedRecipe()
        }
    }

    private fun handleStateChanges() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it ?: return@observe) {
                State.Empty -> handleEmptyResults()
                State.Loading -> {
                }
                State.Done -> {
                    binding.searchResultsRecyclerView.visibility = View.VISIBLE
                    binding.loadingProgressBar.visibility = View.GONE
                }
                else -> hideLoadingBarAndShowError()
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

        binding.errorOccurredImage.setImageResource(R.drawable.empty_inbox)
        binding.errorOccurredDescription.text =
            ResUtils.getResString(context, R.string.no_recipes_found_for_search_query)
    }

    private fun setUpClickListeners() {
        binding.searchResultsBackBtn.setOnClickListener {
            findNavController().popBackStack(R.id.navigation_discover, false)
        }
        binding.searchResultsInput.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setUpRecyclerView() {
        binding.filterRecyclerView.also {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = filterGroupAdapter
        }
        binding.searchResultsRecyclerView.also {
            it.layoutManager = GridLayoutManager(context, 2)
            it.adapter = searchResultsGroupAdapter
        }
    }

    private fun setUpSearchRecyclerViewScrollListener() {
        binding.searchResultsRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
                    val lastPosition = gridLayoutManager.findLastVisibleItemPosition()
                    val searchResultsSize = addedSearchResults.size
                    val diff = searchResultsSize - lastPosition

                    if (diff < 8 && !isLoading) {
                        viewModel.loadComplexSearch(recipeSuggestion.title ?: return)
                        isLoading = true
                    }
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