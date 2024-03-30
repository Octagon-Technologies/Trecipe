package com.octagontechnologies.trecipe.presentation.ui.my_recipes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.octagontechnologies.trecipe.R
import com.octagontechnologies.trecipe.databinding.FragmentMyRecipesBinding
import com.octagontechnologies.trecipe.domain.search.SimpleRecipe
import com.octagontechnologies.trecipe.presentation.ui.my_recipes.tab_layout.SavedTabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyRecipesFragment : Fragment(R.layout.fragment_my_recipes) {

    private val viewModel: MyRecipesViewModel by viewModels()
    private lateinit var binding: FragmentMyRecipesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyRecipesBinding.bind(view)

        setUpTabLayoutAndViewPager()
    }

    private fun setUpTabLayoutAndViewPager() {
        val tabLayout = binding.tabLayout
        val viewPager2 = binding.viewPager
        viewPager2.isSaveEnabled = false
        viewPager2.adapter = MyFragmentStateAdapter()

        val tabConfigurationStrategy =
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = when (position) {
                    0 -> "Recent"
                    1 -> "Liked"
                    2 -> "Saved"
                    else -> throw IndexOutOfBoundsException("Tab position requested: $position")
                }
            }

        TabLayoutMediator(
            tabLayout,
            viewPager2,
            tabConfigurationStrategy
        ).attach()
    }

    inner class MyFragmentStateAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            val openRecipe: (SimpleRecipe) -> Unit = {
                findNavController().navigate(
                    MyRecipesFragmentDirections
                        .actionMyRecipesFragmentToRecipeFragment(it.recipeId)
                )
            }
            return when (position) {
                0 -> SavedTabLayout.getInstance(
                    recipeList = viewModel.recentRecipes,
                    openRecipe = openRecipe
                ) { viewModel.removeFromRecent(it) }

                1 -> SavedTabLayout.getInstance(
                    recipeList = viewModel.likedRecipes,
                    openRecipe = openRecipe
                ) { viewModel.removeFromLiked(it) }

                2 -> SavedTabLayout.getInstance(
                    recipeList = viewModel.savedRecipes,
                    openRecipe = openRecipe
                ) { viewModel.removeFromSaved(it) }

                else -> throw IndexOutOfBoundsException("Position is $position")
            }
        }
    }
}