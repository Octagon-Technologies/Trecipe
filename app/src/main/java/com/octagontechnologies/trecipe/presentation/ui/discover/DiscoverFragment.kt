package com.octagontechnologies.trecipe.presentation.ui.discover

import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.octagontechnologies.trecipe.R
import com.octagontechnologies.trecipe.databinding.FragmentDiscoverBinding
import com.octagontechnologies.trecipe.domain.Resource
import com.octagontechnologies.trecipe.domain.discover.DiscoverRecipe
import com.octagontechnologies.trecipe.presentation.ui.discover.large_discover_card.LargeRecipeGroup
import com.octagontechnologies.trecipe.presentation.ui.recipe.similar_recipe.MiniRecipeGroup
import com.octagontechnologies.trecipe.repo.dto.toSimilarRecipe
import com.octagontechnologies.trecipe.utils.showShortSnackBar
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
    }

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