package com.octagontechnologies.trecipe.presentation.ui.my_recipes.tab_layout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.octagontechnologies.trecipe.R
import com.octagontechnologies.trecipe.databinding.SavedTabLayoutBinding
import com.octagontechnologies.trecipe.domain.search.SimpleRecipe
import com.octagontechnologies.trecipe.presentation.ui.my_recipes.tab_layout.mini_recipe.SavedMiniRecipeGroup
import com.octagontechnologies.trecipe.utils.CustomAnimator
import com.octagontechnologies.trecipe.utils.StackAnimator
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class SavedTabLayout : Fragment(R.layout.saved_tab_layout) {

    private lateinit var binding: SavedTabLayoutBinding

    var recipesList: Flow<List<SimpleRecipe>?> = flowOf<List<SimpleRecipe>?>(listOf())
    var openRecipe: (SimpleRecipe) -> Unit = { }
    var removeFromCategory: (SimpleRecipe) -> Unit = { }

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SavedTabLayoutBinding.bind(view)
        binding.recipesRecyclerview.adapter = groupAdapter
        binding.recipesRecyclerview.itemAnimator = StackAnimator()

        lifecycleScope.launch {
            recipesList.collectLatest { recipesList ->
                Timber.d("recipesList in SavedTabLayout is $recipesList")

                if (recipesList.isNullOrEmpty()) {
                    binding.noRecipesHere.visibility = View.VISIBLE
                    binding.recipesRecyclerview.visibility = View.GONE
                } else {
                    binding.noRecipesHere.visibility = View.GONE
                    binding.recipesRecyclerview.visibility = View.VISIBLE

                    val savedRecipeGroups = recipesList.transformSavedMiniRecipe()

                    groupAdapter.clear()
                    groupAdapter.addAll(savedRecipeGroups)
                }
            }
        }
    }

    private suspend fun List<SimpleRecipe>.transformSavedMiniRecipe() = withContext(Dispatchers.IO) {
        map { simpleRecipe ->
            SavedMiniRecipeGroup(
                simpleRecipe = simpleRecipe,
                openRecipe = { openRecipe(simpleRecipe) },
                removeFromCategory = { removeFromCategory(simpleRecipe) }
            )
        }
    }

    companion object {
        fun getInstance(
            recipeList: Flow<List<SimpleRecipe>?>,
            openRecipe: (SimpleRecipe) -> Unit,
            removeFromCategory: (SimpleRecipe) -> Unit
        ): SavedTabLayout {
            val savedTabLayout = SavedTabLayout()
            savedTabLayout.recipesList = recipeList
            savedTabLayout.openRecipe = openRecipe
            savedTabLayout.removeFromCategory = removeFromCategory
            return savedTabLayout
        }
    }


}