package com.octagon_technologies.trecipe.presentation.ui.discover

import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.FragmentDiscoverBinding
import com.octagon_technologies.trecipe.domain.Resource
import com.octagon_technologies.trecipe.domain.discover.DiscoverRecipe
import com.octagon_technologies.trecipe.presentation.ui.discover.large_discover_card.LargeRecipeGroup
import com.octagon_technologies.trecipe.presentation.ui.recipe.similar_recipe.MiniRecipeGroup
import com.octagon_technologies.trecipe.repo.dto.toSimilarRecipe
import com.octagon_technologies.trecipe.utils.showShortSnackBar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class DiscoverFragment : Fragment(R.layout.fragment_discover) {

    private lateinit var binding: FragmentDiscoverBinding
    private val viewModel by viewModels<DiscoverViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDiscoverBinding.bind(view)

        setUpDiscoverShimmerAndTab()
        setUpTryOutShimmerAndTab()

//        handleStateChanges()
    }

//    private fun handleStateChanges() {
//        viewModel.state.setUpSnackBars(this)
//
//        viewModel.state.observe(viewLifecycleOwner) { state ->
//            when (state ?: return@observe) {
//                State.Loading -> {}
//
//                // Show the recyclerview since we have the results
//                State.Done -> {
//                    binding.dailyInspirationRecyclerview.visibility = View.VISIBLE
//                    binding.tryOutRecyclerview.visibility = View.VISIBLE
//                    binding.dailyProgressBar.visibility = View.GONE
//                    binding.tryOutProgressBar.visibility = View.GONE
//                }
//
//                // In case of any errors, show the user an error message if we have no data in our DB
//                else -> {
//                    if (viewModel.canShowError.value == true) {
//                        if (state == State.ApiError || state == State.NoNetworkError) showError()
//                    } else
//                        showRecyclerViewWithData()
//                }
//            }
//        }
//    }

    /**
     * The shimmer is automatically started { the visibility is already View.VISIBLE from XML }
     *
     * Once the data has been fetched and displayed, we can stop and hide the shimmer and make
     * the recyclerview visible
     */
    private fun setUpDiscoverShimmerAndTab() {
        val shimmer = binding.dailyInspirationShimmer
        shimmer.startShimmer()

        val groupAdapter = GroupAdapter<GroupieViewHolder>()
        binding.dailyInspirationRecyclerview.adapter = groupAdapter

        viewModel.dailyRecipesResult.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success || result.data != null) {
                Timber.d("viewModel.dailyRecipesResult is ${result.data}")
                if (result.data != null) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val discoverRecipes = transformDiscoverRecipes(result.data)

                        withContext(Dispatchers.Main) {
                            Timber.d("Adding discoverRecipes ~ size: ${discoverRecipes.size}")
                            groupAdapter.addAll(discoverRecipes)
                            shimmer.stopShimmer()
                            shimmer.visibility = View.GONE

                            binding.dailyInspirationRecyclerview.visibility = View.VISIBLE
                        }
                    }
                }
            }

            if (result is Resource.Error)
                showShortSnackBar(result.resMessage)
        }
    }

    private suspend fun transformDiscoverRecipes(discoverRecipes: List<DiscoverRecipe>): List<LargeRecipeGroup> =
        withContext(Dispatchers.IO) {
            discoverRecipes.map { discoverRecipe ->
                LargeRecipeGroup(
                    discoverRecipe = discoverRecipe,
                    isSaved = viewModel.isSaved(discoverRecipe),
                    saveOrUnsaveRecipe = { viewModel.saveOrUnSaveRecipe(discoverRecipe) },
                    openRecipe = {
                        findNavController().navigate(
                            DiscoverFragmentDirections.actionDiscoverFragmentToRecipeFragment(
                                discoverRecipe.recipeId
                            )
                        )
                    }
                )
            }
        }


    /**
     * The shimmer is automatically started { the visibility is already View.VISIBLE from XML }
     *
     * Once the data has been fetched and displayed, we can stop and hide the shimmer and make
     * the recyclerview visible
     */
    private fun setUpTryOutShimmerAndTab() {
        val shimmer = binding.tryOutShimmer
        shimmer.startShimmer()

        var isLoading = false

        binding.root.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            val nestedScrollView = checkNotNull(v) {
                return@setOnScrollChangeListener
            }

            val lastChild = nestedScrollView.getChildAt(nestedScrollView.childCount - 1)

            if (lastChild != null) {
                if ((scrollY >= (lastChild.measuredHeight - nestedScrollView.measuredHeight)) && scrollY > oldScrollY && !isLoading) {
                    //get more items
                    isLoading = true
                    viewModel.fetchTryOutRecipes()
                    isLoading = false
                }
            }
        }

        val groupAdapter = GroupAdapter<GroupieViewHolder>()
        binding.tryOutRecyclerview.adapter = groupAdapter

        /**
         * The network call might be successful hence Resource.Success
         *    OR
         * We have some data cached in the DB hence the network call might fail but we can still
         * present some data
         */
        viewModel.tryRecipesResult.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success || result.data != null) {
                Timber.d("viewModel.tryRecipesResult is ${result.data}")
                if (result.data != null) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val tryOutRecipes = transformTryOutRecipes(result.data)

                        withContext(Dispatchers.Main) {
                            groupAdapter.addAll(tryOutRecipes)
                            shimmer.stopShimmer()
                            shimmer.visibility = View.GONE

                            binding.tryOutRecyclerview.visibility = View.VISIBLE
                        }
                    }
                }
            }

            if (result is Resource.Error)
                showShortSnackBar(result.resMessage)
        }

//        val adapter = TryOutPagingAdapter(
//            openRecipe = { recipeId ->
//                findNavController().navigate(
//                    DiscoverFragmentDirections
//                        .actionDiscoverFragmentToRecipeFragment(recipeId)
//                )
//            },
//            saveOrUnsaveRecipe = {  discoverRecipe ->
//                viewModel.saveOrUnSaveRecipe(discoverRecipe)
//            }
//        )
//
//        binding.tryOutRecyclerview.adapter = adapter
//
//        adapter.addLoadStateListener {  loadStates ->
//            Timber.d("loadStates.prepend is ${loadStates.prepend}")
//            Timber.d("loadStates.append is ${loadStates.append}")
//            Timber.d("loadStates.refresh is ${loadStates.refresh}")
//        }

//        viewModel.tryOutPagingData.observe(viewLifecycleOwner) { pagingRecipes ->
//            Timber.d("viewModel.tryRecipesResult is $pagingRecipes")
//
//            if (pagingRecipes != null) {
//                adapter.submitData(lifecycle, pagingRecipes)
//
//                if (shimmer.isShimmerVisible) {
//                    shimmer.stopShimmer()
//                    shimmer.visibility = View.GONE
//                }
//                binding.tryOutRecyclerview.visibility = View.VISIBLE
//            }
//        }
    }

    private suspend fun transformTryOutRecipes(tryOutRecipes: List<DiscoverRecipe>) =
        withContext(Dispatchers.IO) {
            tryOutRecipes.map { discoverRecipe ->
                MiniRecipeGroup(
                    similarRecipe = discoverRecipe.toSimilarRecipe(),
                    isSaved = viewModel.isSaved(discoverRecipe),
                    openRecipe = {
                        findNavController().navigate(
                            DiscoverFragmentDirections.actionDiscoverFragmentToRecipeFragment(
                                discoverRecipe.recipeId
                            )
                        )
                    },
                    saveOrUnsaveRecipe = { viewModel.saveOrUnSaveRecipe(discoverRecipe) }
                )
            }
        }

}
/*
TODO: Add recyclerview tracking for loading more recipes when it reaches close to the end

        binding.discoverRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition()
                    val recipesSize = viewModel.listOfRandomRecipe.value?.size ?: return
                    val diff = recipesSize - lastVisiblePosition
                    Timber.d("RecyclerView.diff is $diff")

                    if (diff < 5 && !isLoading) {
                        viewModel.loadData()
                        isLoading = true
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Timber.d("dy is $dy in DiscoverFragment")

                // if dy > 0, the user is scrolling down
                if (dy > 0)
                    BottomNavUtils.hideBottomNavView(activity)
                else if (dy < 0)
                    BottomNavUtils.showBottomNavView(activity)
            }
        })

 */