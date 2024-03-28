package com.octagon_technologies.trecipe.presentation.ui.my_recipes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.FragmentMyRecipesBinding
import com.octagon_technologies.trecipe.presentation.ui.my_recipes.tab_layout.SavedTabLayout
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

        tabLayout.setSelectedTabIndicator(R.drawable.tab_indicator)
        TabLayoutMediator(
            tabLayout,
            viewPager2,
            tabConfigurationStrategy
        ).attach()
    }

    inner class MyFragmentStateAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment =
            when (position) {
                0 -> SavedTabLayout.getInstance(viewModel.recentRecipes)
                1 -> SavedTabLayout.getInstance(viewModel.likedRecipes)
                2 -> SavedTabLayout.getInstance(viewModel.savedRecipes)
                else -> throw IndexOutOfBoundsException("Position is $position")
            }
    }
}