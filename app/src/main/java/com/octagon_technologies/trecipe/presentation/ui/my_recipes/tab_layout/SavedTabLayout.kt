package com.octagon_technologies.trecipe.presentation.ui.my_recipes.tab_layout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.SavedTabLayoutBinding
import com.octagon_technologies.trecipe.domain.search.SimpleRecipe
import com.octagon_technologies.trecipe.presentation.ui.my_recipes.tab_layout.mini_recipe.SavedMiniRecipeGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SavedTabLayout : Fragment(R.layout.saved_tab_layout) {

    private lateinit var binding: SavedTabLayoutBinding
    private val viewModel by viewModels<SavedTabViewModel>()

    var recipesList: LiveData<List<SimpleRecipe>?> = MutableLiveData<List<SimpleRecipe>?>()

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SavedTabLayoutBinding.bind(view)
        binding.recipesRecyclerview.adapter = groupAdapter

        recipesList.observe(viewLifecycleOwner) { recipesList ->
            Timber.d("recipesList in SavedTabLayout is $recipesList")

            if (recipesList.isNullOrEmpty()) {
                binding.noRecipesHere.visibility = View.VISIBLE
                binding.recipesRecyclerview.visibility = View.GONE
            } else {
                binding.noRecipesHere.visibility = View.GONE
                binding.recipesRecyclerview.visibility = View.VISIBLE

                groupAdapter.clear()
                recipesList.forEach { simpleRecipe ->
                    val savedMiniRecipeGroup = SavedMiniRecipeGroup(
                        simpleRecipe = simpleRecipe,
                        isSaved = viewModel.isSaved(simpleRecipe),
                        saveOrUnSaveRecipe = { viewModel.saveOrUnSaveRecipe(simpleRecipe) })

                    groupAdapter.add(savedMiniRecipeGroup)
                }
            }
        }

    }

    companion object {
        fun getInstance(
            recipeList: LiveData<List<SimpleRecipe>?>?
        ): SavedTabLayout {
            val savedTabLayout = SavedTabLayout()
            savedTabLayout.recipesList = recipeList ?: liveData {}
            return savedTabLayout
        }
    }


}